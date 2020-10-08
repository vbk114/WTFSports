package com.e5ctech.wtfsports.accounts.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.dashboard.activities.GenericWebViewActivity
import com.e5ctech.wtfsports.dashboard.model.Articles
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ArticlesAdapter(
    var articleList: MutableList<Articles>,
    val activity: AppCompatActivity,
    var onItemClickListner: onItemClickListener
) : RecyclerView.Adapter<ArticlesAdapter.CustomHolder>() {


    override fun getItemCount(): Int {
        return articleList.size
    }

    interface onItemClickListener {
        fun itemClick(position: Int, userPics: String, isSelected: Boolean)
    }

    interface onItemDeleteClickListener {
        fun itemDeleteClick(position: Int, userPics: String)
    }

    override fun onBindViewHolder(holder: CustomHolder, position: Int) {

        val articles = articleList[holder.adapterPosition]
        holder.bindProducts(activity, articles);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_articles_layer_one, parent, false)
        return CustomHolder(v)
    }

    class CustomHolder(v: View) : RecyclerView.ViewHolder(v) {


        val tvSourceAuthor = itemView.findViewById(R.id.tvSourceAuthor) as TextView
        val ivaArticleImage = itemView.findViewById(R.id.ivaArticleImage) as ImageView
        val tvTitle = itemView.findViewById(R.id.tvTitle) as TextView
        val tvTimeAndAuthor = itemView.findViewById(R.id.tvTimeAndAuthor) as TextView
        val tvDescription = itemView.findViewById(R.id.tvDescription) as TextView

        fun bindProducts(
            activity: AppCompatActivity,
            articles: Articles
        ) {

            tvSourceAuthor.text = articles.author
            tvTitle.text = articles.title
            if(articles.description!=null && articles.description.isNotEmpty()){
                tvDescription.visibility = View.VISIBLE
                tvDescription.text = articles.description
            }else{
                tvDescription.visibility = View.GONE
            }
            tvTimeAndAuthor.text = articles.publishedAt
            if(articles.urlToImage !=null && articles.urlToImage.isNotEmpty()){

                Picasso.get()
                    .load(articles.urlToImage)
                    .into(ivaArticleImage, object:Callback{
                        override fun onSuccess() {
                            ivaArticleImage.visibility = View.VISIBLE
                        }
                        override fun onError(e:Exception) {
                            ivaArticleImage.visibility = View.GONE
                        }
                    })
            }else{
                ivaArticleImage.visibility = View.GONE
            }

            itemView.setOnClickListener {
                if(articles.urlToImage!=null && articles.urlToImage.isNotEmpty()){
                    val webviewIntent = Intent(activity,GenericWebViewActivity::class.java)
                    webviewIntent.putExtra("url",articles.url)
                    activity.startActivity(webviewIntent)
                }
            }

        }
    }

}