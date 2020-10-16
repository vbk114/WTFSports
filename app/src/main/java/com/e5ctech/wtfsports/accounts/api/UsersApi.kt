package com.e5ctech.wtfsports.accounts.api

import bibou.biboubeauty.com.utils.networking.*
import com.e5ctech.wtfsports.accounts.models.UpdateUserResponse
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.dashboard.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UsersApi {

    @Headers("Content-Type: application/json")
    @POST("auth/register/")
    fun saveUser(@Body users: Users,@Header("Authorization") token :String): Call<DefaultResponse>

    @Headers("Content-Type: application/json")
    @PUT("main/update-user-text/{id}/")
    fun updateUser(@Path("id") userId:String,@Body users: UpdateUser): Call<UpdateUserResponse>

    @Headers("Content-Type: application/json")
    @PUT("main/update-user-profile-image/{id}/")
    fun updateProfileImage(@Path("id") userId:String,
    @Body users: Users): Call<DefaultResponse>

    @Headers("Content-Type: application/json")
    @PUT("main/update-user-cover-image/{id}/")
    fun updateCoverImage(@Path("id") userId:String,
                         @Body users: Users): Call<DefaultResponse>

    @Headers("Content-Type: application/json")
    @POST("main/user-post/{id}/")
    fun savePost(@Path("id") id:String, @Body feeds: AddPost): Call<FeedsResponse>

    @Headers("Content-Type: application/json")
    @PUT("main/update-post/{id}/")
    fun updatePost(@Path("id") id:Int, @Body feeds: AddPost): Call<FeedsResponse>


    @POST("main/share-post/")
    fun sharePost(@Body feeds: SharePost): Call<FeedsResponse>

    @Headers("Content-Type: application/json")
    @DELETE("main/delete-post/{id}/")
    fun deletePost(@Path("id") id:Int): Call<FeedsResponse>

    @POST("auth/login/")
    fun loginUser(@Body users: Users): Call<DefaultResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/otp-verify/")
    fun otpverify(@Body users: Users): Call<DefaultResponse>

    @Headers("Content-Type: application/json")
    @GET("main/get-sports-choice/")
    fun getSportsChoiceList(): Call<TeamSelectionResponse>

    @GET("main/get-user-profile/{id}/")
    fun getUserProfile(@Path("id") userId:String): Call<DefaultResponse>

    @Headers("Content-Type: application/json")
    @POST("main/get-personal-feeds/")
    fun getFeedsResponse(@Body feedsResponse: FeedsParams): Call<FeedsResponse>

    @Headers("Content-Type: application/json")
    @POST("main/comment-post/{postid}/{userid}")
    fun commentPostResponse(@Path("postid") postid:Int, @Path("userid") userId:String,
                            @Body feedsResponse: CommentPostParams): Call<DefaultResponse>

    @Headers("Content-Type: application/json")
    @POST("main/like-post/")
    fun likePostResponse(@Body feedsResponse: LikePostParams): Call<LikePostResponse>

    @Headers("Content-Type: application/json")
    @POST("main/get-personal-profile-feeds/")
    fun getUserFeedsResponse(@Body feedsResponse: FeedsParams): Call<FeedsResponse>

    @Headers("Content-Type: application/json")
    @GET("auth/country/")
    fun getCountryList(): Call<CountryResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/request-reset-email-send-otp/")
    fun resetRequest(@Body users: Users): Call<DefaultResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/otp-verify-for-password-reset/")
    fun restOtpverify(@Body users: Users): Call<OtpResponse>

    @Headers("Content-Type: application/json")
    @PATCH("auth/set-new-password/")
    fun setnewPassword(@Body users: Users): Call<OtpResponse>

}