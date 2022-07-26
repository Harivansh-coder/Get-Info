package com.harivansh.gitinfo.model

import com.google.gson.annotations.SerializedName

class Repo(
    @field:SerializedName("name") var repoName: String, @field:SerializedName(
        "description"
    ) var repoDescription: String
)