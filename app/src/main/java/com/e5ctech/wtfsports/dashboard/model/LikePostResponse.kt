package com.e5ctech.wtfsports.dashboard.model

data class LikePostResponse(var status: Boolean,
                              var message: String,
                              var Response: CResponse) {

    data class CResponse(var id: Int,
                         var likepostid: Int,
                         var likeuserid: Int,
                         var like: Boolean,
                         var totallikes: Int){

    }
}