package com.e5ctech.wtfsports.dashboard.model

import com.e5ctech.wtfsports.accounts.models.Sports
import java.io.Serializable

class Feeds : Serializable  {

    /*{
    "id": 1,
            "userid": 3,
            "postimage": "/media/adimage/81c66d93-eda.jpg",
            "posttext": "hello",
            "postid": []
}*/
    var id: Int = 0
    var userid : String = ""
    var username: String = ""
    var profile_image: String= ""
    var postimage =""
    var posttext=""
    var post_time: String =""
    var like_commnet_count: Int = 0
    var islike: Boolean = false
    var shareuserid:Int = 0
    var comments: MutableList<Comment>? = null
}

class Comment : Serializable{
    var comment_id:Int =0
    var comment_user_id:Int = 0
    var comment_username: String = ""
    var comment_profile_image: String = ""
    var comment: String = ""
    var comment_time: String = ""

}