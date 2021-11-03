package com.harivansh.gitinfo.api;

import com.harivansh.gitinfo.model.Issue;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface IssueApi {

    @GET("issues")
    Call<List<Issue>> listIssue();
}
