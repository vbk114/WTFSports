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
    var userid : Int=0
    var postimage =""
    var posttext=""
    var postid =""
    var shareuserid=""
    var is_like: Boolean = false
}