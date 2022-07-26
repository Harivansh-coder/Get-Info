package com.harivansh.gitinfo

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.harivansh.gitinfo.adapter.RepoAdapter
import com.harivansh.gitinfo.adapter.RepoAdapter.RepoViewClickListener
import com.harivansh.gitinfo.api.RepoApi
import com.harivansh.gitinfo.databinding.ActivityMainBinding
import com.harivansh.gitinfo.model.Repo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var repoArrayList: ArrayList<Repo>? = null
    private var listener: RepoViewClickListener? = null
    private var repoAdapter: RepoAdapter? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)
        progressDialog = ProgressDialog(this@MainActivity)

        // repo list
        repoArrayList = ArrayList()
        progressDialog!!.setMessage("Loading")

        // get repo button
        binding!!.getrepo.setOnClickListener {
            // getting username from the edittext
            val userName = binding!!.gitUsername.text.toString().trim()
            if (userName.isNotEmpty()) {
                progressDialog!!.show()
                repoArrayList!!.clear() // clear the array before adding any element
                requestService(userName)
            } else Snackbar.make(
                binding!!.getrepo,
                getString(R.string.empty_username),
                BaseTransientBottomBar.LENGTH_LONG
            ).show()
        }
    }

    private fun setAdapter() {
        setOnClickListner()
        repoAdapter = RepoAdapter(repoArrayList!!, listener!!)
        binding!!.reposRecycleView.layoutManager = LinearLayoutManager(this)
        binding!!.reposRecycleView.itemAnimator = DefaultItemAnimator()
        binding!!.reposRecycleView.adapter = repoAdapter
    }

    // function to pass data to other activity
    private fun setOnClickListner() {
        listener = object : RepoViewClickListener {
            override fun onClick(view: View?, position: Int) {
                val intent = Intent(applicationContext, IssueScreen::class.java)
                val bundle = Bundle()
                bundle.putString("userName", binding!!.gitUsername.text.toString())
                bundle.putString("repoName", repoArrayList!![position].repoName)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    // getting the repo data from the api
    private fun requestService(userName: String) {
        val url = "https://api.github.com/"
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val repoApi = retrofit.create(RepoApi::class.java)
        val call = repoApi.listRepos(userName)
        call!!.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>?>, response: Response<List<Repo>?>) {
                progressDialog!!.dismiss()
                val repos = response.body()
                if (response.isSuccessful) {
                    assert(repos != null)
                    repoArrayList!!.addAll(repos!!)
                }
                setAdapter()


                //Toast.makeText(MainActivity.this,repos.get(0).getRepoName(),Toast.LENGTH_LONG).show();
            }

            override fun onFailure(call: Call<List<Repo>?>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}