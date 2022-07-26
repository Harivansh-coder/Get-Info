package com.harivansh.gitinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harivansh.gitinfo.R
import com.harivansh.gitinfo.model.Repo
import java.util.ArrayList

class RepoAdapter(
    private val repoArrayList: ArrayList<Repo>,
    private val listener: RepoViewClickListener
) :
    RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {
    inner class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val repoName: TextView
        val repoDesc: TextView
        override fun onClick(v: View) {
            listener.onClick(v, adapterPosition)
        }

        init {
            repoName = view.findViewById(R.id.repoName)
            repoDesc = view.findViewById(R.id.repoDesc)
            view.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repo_row, parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repoName = repoArrayList[position].repoName
        val repoDesc = repoArrayList[position].repoDescription
        holder.repoName.text = repoName
        holder.repoDesc.text = repoDesc
    }

    override fun getItemCount(): Int {
        return repoArrayList.size
    }

    interface RepoViewClickListener {
        fun onClick(view: View?, position: Int)
    }
}