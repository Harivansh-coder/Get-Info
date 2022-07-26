package com.harivansh.gitinfo

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harivansh.gitinfo.adapter.IssueAdapter
import com.harivansh.gitinfo.api.IssueApi
import com.harivansh.gitinfo.databinding.ActivityIssueScreenBinding
import com.harivansh.gitinfo.model.Issue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class IssueScreen : AppCompatActivity() {
    private var binding: ActivityIssueScreenBinding? = null
    private var issueArrayList: ArrayList<Issue>? = null
    private var repoName: String? = "Repo"
    private var userName: String? = "user"
    private var url = "https://api.github.com/repos/"
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIssueScreenBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)

        // issue array
        issueArrayList = ArrayList()
        progressDialog = ProgressDialog(this@IssueScreen)
        progressDialog!!.setMessage("Getting issues for this repository")
        val extras = intent.extras
        if (extras != null) {
            userName = extras.getString("userName")
            repoName = extras.getString("repoName")
        }

        // repo name
        binding!!.repoNameissue.text = repoName
        Log.d("reponame", repoName!!)


        // getting issue from api
        url += "$userName/$repoName/"
        issues
    }

    private val issues: Unit
        get() {
            progressDialog!!.show()
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val issueApi = retrofit.create(IssueApi::class.java)
            val issueCall = issueApi.listIssue()

            issueCall.enqueue(object : Callback<List<Issue>> {
                override fun onResponse(
                    call: Call<List<Issue>?>,
                    response: Response<List<Issue>?>
                ) {
                    val issueList = response.body()
                    if (response.isSuccessful) {
                        assert(issueList != null)
                        issueArrayList!!.addAll(issueList!!)
                    }
                    setAdapter()
                    progressDialog!!.dismiss()
                    if (issueArrayList!!.isEmpty()) {
                        Toast.makeText(this@IssueScreen, "No issue in this repo", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<List<Issue>?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    Toast.makeText(this@IssueScreen, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }

    private fun setAdapter() {
        val issueAdapter = IssueAdapter(issueArrayList!!)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding!!.issuesRecycleView.layoutManager = layoutManager
        binding!!.issuesRecycleView.itemAnimator = DefaultItemAnimator()
        binding!!.issuesRecycleView.adapter = issueAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}