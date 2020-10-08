package com.e5ctech.wtfsports.accounts.models

import java.io.Serializable


/*{
    "email": "supreeeiyaaaraj@gmail.com",
    "fullname":"supeeraaiya raj sao",
    "gender":"FEMALE",
    "contactno":"212309586",
     "location":"bangalore",
     "dob":"1940-08-02",
    "password":"123456"
}*/

/*"id": 11,
        "email": "supreeeiyaaaraj@gmail.com",
        "contactno": "212309586",
        "fullname": "supeeraaiya raj sao",
        "location": "bangalore",
        "dob": "1940-08-02",
        "gender": "FEMALE",
        "otp": "449588",
        "is_verified": false*/

class Users (
    var id: String? =null,
    var fullname: String? = null,
    var gender: String? = null,
    var email: String? = null,
    var dob:String?=null,
    var location:String?=null,
    var contactno: String? = null,
    var password: String? = null,
    var otp:String?=null,
    var tokens: Tokens = Tokens(),
    var uidb64:String?=null,
    var token:String?=null,
    var profile_image:String?=null,
    var cover_image:String?=null,
    var isVerified:String = ""
): Serializable {
}