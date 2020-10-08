package com.e5ctech.wtfsports.dashboard.model

import com.e5ctech.wtfsports.accounts.models.Sports
import java.io.Serializable

class FeedsResponse : Serializable  {

    /*{
    "status": true,
    "Response": [
        {
            "id": 1,
            "userid": 3,
            "postimage": "/media/adimage/81c66d93-eda.jpg",
            "posttext": "hello",
            "postid": []
        }
    ]
}*/

    var status: Boolean =false
    var Responce:MutableList<Feeds> ? = null
    var Response:Feeds?=null
    var message : String=""
    var senderid:String = ""
}