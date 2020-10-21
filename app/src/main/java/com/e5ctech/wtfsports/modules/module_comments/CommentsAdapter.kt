package com.e5ctech.wtfsports.modules.module_comments

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.e5ctech.wtfsports.BuildConfig
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.dashboard.model.Comment
import com.e5ctech.wtfsports.databinding.ItemCommentBinding
import com.e5ctech.wtfsports.utils.Utils
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CommentsAdapter(context: Context, comments: MutableList<Comment>?) :
    RecyclerView.Adapter<CommentsAdapter.CommentsHolder>(){

    private var context: Context
    private var comments: MutableList<Comment>
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

    init {
        this.context = context
        this.comments = comments!!
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
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
        holder.binding.username.setText(comments.get(position).comment_username)
        holder.binding.msg.setText(comments.get(position).comment)

        var time : String? = null
        try {
            time = Utils.covertTimeToText1(comments.get(position).comment_time)
            Log.e("timetry", ":::" + time + "---" + comments.get(position).comment_time)
        } catch (e: ParseException) {
            time = comments.get(position).comment_time
            Log.e("timecatch", ":::" + time + "---" + comments.get(position).comment_time)
        }
        holder.binding.time.text = time

        Picasso.get()
            .load(BuildConfig.APP_HOST+comments.get(position).comment_profile_image)
            .into(holder.binding.profilePic, object: Callback {
                override fun onSuccess() {
                    holder.binding.profilePic.visibility = View.VISIBLE
                }
                override fun onError(e:Exception) {
                    holder.binding.profilePic.setImageResource(R.drawable.userimage)
                    //ivFeedsImage.visibility = View.GONE
                }
            })
    }

    override fun getItemCount(): Int {
        return  comments.size
    }

}