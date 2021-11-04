package com.harivansh.gitinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.harivansh.gitinfo.adapter.IssueAdapter;
import com.harivansh.gitinfo.api.IssueApi;
import com.harivansh.gitinfo.databinding.ActivityIssueScreenBinding;
import com.harivansh.gitinfo.model.Issue;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IssueScreen extends AppCompatActivity {

    private ActivityIssueScreenBinding binding;

    private ArrayList<Issue> issueArrayList;

    private String repoName = "Repo";
    private String userName = "user";

    private String url = "https://api.github.com/repos/";


    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIssueScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // issue array

        issueArrayList = new ArrayList<>();

        progressDialog = new ProgressDialog(IssueScreen.this);
        progressDialog.setMessage("Getting issues for this repository");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("userName");
            repoName = extras.getString("repoName");
        }

        // repo name
        binding.repoNameissue.setText(repoName);

        Log.d("reponame", repoName);


        // getting issue from api

        url += userName+"/"+repoName+"/";

        getIssues();


    }

    private void getIssues() {

        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IssueApi issueApi = retrofit.create(IssueApi.class);

        Call<List<Issue>> issueCall = issueApi.listIssue();

        issueCall.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {


                List<Issue> issueList = response.body();

                if (response.isSuccessful()){

                    assert issueList != null;
                    issueArrayList.addAll(issueList);
                }
                setAdapter();
                progressDialog.dismiss();

                if (issueArrayList.isEmpty()){
                    Toast.makeText(IssueScreen.this,"No issue in this repo",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<List<Issue>> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(IssueScreen.this, t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setAdapter(){
        IssueAdapter issueAdapter = new IssueAdapter(issueArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        binding.issuesRecycleView.setLayoutManager(layoutManager);
        binding.issuesRecycleView.setItemAnimator(new DefaultItemAnimator());
        binding.issuesRecycleView.setAdapter(issueAdapter);

    }


    @Override
    protected void onDestroy () {
        super.onDestroy();
        binding = null;
    }
}