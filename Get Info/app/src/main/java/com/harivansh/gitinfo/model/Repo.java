package com.harivansh.gitinfo.model;

import com.google.gson.annotations.SerializedName;

public class Repo {

    @SerializedName("name")
    private String repoName;

    @SerializedName("description")
    private String repoDescription;

    public Repo(String repoName, String repoDescription) {
        this.repoName = repoName;
        this.repoDescription = repoDescription;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoDescription() {
        return repoDescription;
    }

    public void setRepoDescription(String repoDescription) {
        this.repoDescription = repoDescription;
    }
}
