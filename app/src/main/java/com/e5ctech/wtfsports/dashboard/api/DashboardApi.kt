package com.e5ctech.wtfsports.accounts.api

import bibou.biboubeauty.com.utils.networking.DefaultResponse
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.dashboard.model.ArticleResponse
import com.e5ctech.wtfsports.dashboard.model.Articles
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface DashboardApi {

    @Headers("Content-Type: application/json")
    @GET("main/get-feeds-public/")
    fun getDashboardFeeds(): Call<DefaultResponse>

}