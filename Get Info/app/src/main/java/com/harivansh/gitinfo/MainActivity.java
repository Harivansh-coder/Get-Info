package com.harivansh.gitinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.harivansh.gitinfo.adapter.RepoAdapter;
import com.harivansh.gitinfo.api.RepoApi;
import com.harivansh.gitinfo.databinding.ActivityMainBinding;
import com.harivansh.gitinfo.model.Repo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ArrayList<Repo> repoArrayList;
    private RepoAdapter.RepoViewClickListener listener;

    private RepoAdapter repoAdapter;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        progressDialog = new ProgressDialog(MainActivity.this);

        // repo list
        repoArrayList = new ArrayList<>();

        progressDialog.setMessage("Loading");

        // get repo button
        binding.getrepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog.show();
                // getting username from the edittext
                String userName = binding.gitUsername.getText().toString().trim();

                if (userName.length() != 0){

                    repoArrayList.clear(); // clear the array before adding any element
                    requestService(userName);


                }else Snackbar.make(binding.getrepo,
                        getString(R.string.empty_username),
                        BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

    }

    private void setAdapter() {
        setOnClickListner();
        repoAdapter = new RepoAdapter(repoArrayList,listener);
        binding.reposRecycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.reposRecycleView.setItemAnimator(new DefaultItemAnimator());
        binding.reposRecycleView.setAdapter(repoAdapter);
        
    }


    // function to pass data to other activity
    private void setOnClickListner() {

        listener = new RepoAdapter.RepoViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(),IssueScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("userName",binding.gitUsername.getText().toString());
                bundle.putString("repoName",repoArrayList.get(position).getRepoName());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        };
    }


    // getting the repo data from the api
    private void requestService(String userName){

        String url ="https://api.github.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RepoApi repoApi = retrofit.create(RepoApi.class);

        Call<List<Repo>> call = repoApi.listRepos(userName);
        call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {

                progressDialog.dismiss();
                List<Repo> repos = response.body();

                if (response.isSuccessful()){

                    assert repos != null;
                    repoArrayList.addAll(repos);
                }
                setAdapter();



                //Toast.makeText(MainActivity.this,repos.get(0).getRepoName(),Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}