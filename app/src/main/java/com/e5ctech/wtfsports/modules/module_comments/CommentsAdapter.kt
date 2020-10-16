package com.e5ctech.wtfsports.modules.module_comments

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.databinding.ItemCommentBinding

class CommentsAdapter(context: Context) :
    RecyclerView.Adapter<CommentsAdapter.CommentsHolder>(){

    private var context: Context

    init {
        this.context = context
    }
    data class CommentsHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsHolder {
        return CommentsHolder(
            DataBindingUtil
                .inflate<ItemCommentBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_comment,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
        holder.binding.username.setText("User" + position + 1)
        holder.binding.msg.setText("Message" + position + 1)
    }

    override fun getItemCount(): Int {
        return  8
    }

}