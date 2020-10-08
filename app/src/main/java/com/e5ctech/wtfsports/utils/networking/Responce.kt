package com.e5ctech.wtfsports.utils.networking

import java.io.Serializable

class Responce(  var id: Int? = 0,
                 var fullname: String? = null,
                 var gender: String? = null,
                 var email: String? = null,
                 var dob:String?=null,
                 var location:String?=null,
                 var contactno: String? = null,
                 var password: String? = null,
                 var otp:String?=null,
                 var tokens:String?=null,
                 var isVerified:String?=null):Serializable {

    /* "id": "OA",
        "email": "tmlrsn53@gmail.com",
        "contactno": "9685748596",
        "tokens": "{'refresh': 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoicmVmcmVzaCIsImV4cCI6MTYwMTU2MTI1NywianRpIjoiODVmZWRiZmY3YTUzNDBkZThiNDI4ZjgyNmNmZDc0NGQiLCJ1c2VyX2lkIjo4fQ.4vz-qk2L2P4ifTWfmp-JT7-RTy9vpgiQosege1Cfu7g', 'access': 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjAxNDc2NjU3LCJqdGkiOiJkMGUwOWUxOWMzMTU0NWNlODA1ZTMzNzE1NWU0YmYyMCIsInVzZXJfaWQiOjh9.PZdByGgka8qNil2aTe06cjcgWIGigsO6-hfmtrcZDMc'}",
        "fullname": "Tamil",
        "location": "Bangalore",
        "dob": "1991-07-19",
        "gender": "MALE",
        "isVerified": "True"*/

}