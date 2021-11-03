package com.harivansh.gitinfo.model;

import com.google.gson.annotations.SerializedName;

public class Issue {

    @SerializedName("title")
    private String issueName;

    public Issue(String issueName) {
        this.issueName = issueName;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }
}
