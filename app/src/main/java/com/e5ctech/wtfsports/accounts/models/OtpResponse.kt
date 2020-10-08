package bibou.biboubeauty.com.utils.networking

import java.io.Serializable

class OtpResponse : Serializable  {

    var status: String =""
    var Responce:String = ""
    var detail : String=""
    var uidb64:String?=null
    var token:String?=null
}