package com.e5ctech.wtfsports.dashboard.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bibou.biboubeauty.com.utils.networking.BibouApiClient
import bibou.biboubeauty.com.utils.networking.DefaultResponse
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.adapters.ArticlesAdapter
import com.e5ctech.wtfsports.dashboard.model.ArticleResponse
import com.e5ctech.wtfsports.utils.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : BaseFragment(), ArticlesAdapter.onItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var articlesAdapter: ArticlesAdapter
    lateinit var rvArticles:RecyclerView
    lateinit var swHome:SwipeRefreshLayout

    override fun setUp() {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        getArticleResponse()
        rvArticles = view.findViewById(R.id.rvArticles)
        swHome = view.findViewById(R.id.swHome)

        swHome.setOnRefreshListener(this)

        return view;
    }

    fun getArticleResponse() {
        showProgressDialog()
        val call = BibouApiClient
            .instance(getBaseActivity()!!)
            .dashboardApi.getDashboardFeeds()

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {

                if (response.isSuccessful && response.body() != null) {
                    val articleResponse = response.body();
                    val articles = articleResponse!!.Response
                    articlesAdapter = ArticlesAdapter(
                        articleResponse.Response.articles,
                        getBaseActivity()!!, this@HomeFragment
                    )
                    rvArticles.adapter = articlesAdapter
                    dismissProgressDialog()

                } else {
                    dismissProgressDialog()

                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                dismissProgressDialog()
                Toast.makeText(
                    requireContext(),
                    "error occured",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })
    }

    override fun itemClick(position: Int, userPics: String, isSelected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onRefresh() {
        Handler().postDelayed(Runnable {
            getArticleResponse()
            swHome.isRefreshing = false
        }, 2000)

    }

}