package com.harivansh.gitinfo.api

import com.harivansh.gitinfo.model.Issue
import retrofit2.Call
import retrofit2.http.GET

interface IssueApi {
    @GET("issues")
    fun listIssue(): Call<List<Issue>>
}