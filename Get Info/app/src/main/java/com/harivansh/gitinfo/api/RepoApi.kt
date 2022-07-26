package com.harivansh.gitinfo.api

import com.harivansh.gitinfo.model.Repo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RepoApi {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String?): Call<List<Repo>>
}