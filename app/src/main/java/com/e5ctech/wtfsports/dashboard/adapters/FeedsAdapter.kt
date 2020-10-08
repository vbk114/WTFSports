package com.e5ctech.wtfsports.accounts.adapters

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.e5ctech.wtfsports.BuildConfig
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.dashboard.model.Feeds
import com.e5ctech.wtfsports.utils.base.BaseActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class FeedsAdapter(var feedsList:MutableList<Feeds>,
                        val activity: BaseActivity,
                   var onitemMenuSelectedListener: onItemMenuSelectedListener,
) : RecyclerView.Adapter<FeedsAdapter.CustomHolder>() {

    override fun getItemCount(): Int {
        return feedsList.size
    }

    interface onItemMenuSelectedListener {
        fun onItemMenuClick(feeds: Feeds)
    }

    override fun onBindViewHolder(holder: CustomHolder, position: Int) {
        val feeds = feedsList[holder.adapterPosition]

        holder.bindProducts(activity,onitemMenuSelectedListener, feeds);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_feeds, parent, false)
        return CustomHolder(v)
    }

    class CustomHolder(v: View) : RecyclerView.ViewHolder(v) {

        val ivFeedsUserIMage = itemView.findViewById(R.id.ivFeedsUserIMage) as ImageView
        val ivFeedsImage = itemView.findViewById(R.id.ivFeedsImage) as ImageView
        val ivMenu = itemView.findViewById(R.id.ivMenu) as ImageView
        val tvName = itemView.findViewById(R.id.tvName) as TextView
        val tvTime = itemView.findViewById(R.id.tvTime) as TextView
        val tvFeedDetails = itemView.findViewById(R.id.tvFeedDetails) as TextView
        val tvLikeCommentCountNo = itemView.findViewById(R.id.tvLikeCommentCountNo) as TextView

        fun bindProducts(
            activity: BaseActivity,
            onitemMenuSelectedListener: onItemMenuSelectedListener,
           feeds: Feeds
        ) {

            tvName.text = activity.getUsersLocally().fullname
            tvFeedDetails.text = feeds.posttext
            tvTime.text = "10.30 PM"

            Picasso.get()
                .load(BuildConfig.APP_HOST+feeds.postimage)
                .into(ivFeedsImage, object: Callback {
                    override fun onSuccess() {
                        ivFeedsImage.visibility = View.VISIBLE
                    }
                    override fun onError(e:Exception) {
                        ivFeedsImage.visibility = View.GONE
                    }
                })


            ivMenu.setOnClickListener {
                onitemMenuSelectedListener.onItemMenuClick(feeds)
            }

        }
    }
}