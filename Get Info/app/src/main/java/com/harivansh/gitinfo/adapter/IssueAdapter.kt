package com.harivansh.gitinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harivansh.gitinfo.R
import com.harivansh.gitinfo.model.Issue

class IssueAdapter(private val issueArrayList: ArrayList<Issue>) :
    RecyclerView.Adapter<IssueAdapter.IssueViewHolder>() {
    inner class IssueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val issueName: TextView

        init {
            issueName = view.findViewById(R.id.issues_name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.issue_row, parent, false)
        return IssueViewHolder(view)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        val issueName = issueArrayList[position].issueName
        holder.issueName.text = issueName
    }

    override fun getItemCount(): Int {
        return issueArrayList.size
    }
}