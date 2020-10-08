package bibou.biboubeauty.com.utils.networking

import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.dashboard.model.ArticleResponse
import java.io.Serializable

class DefaultResponse(
    var status: String ="",
    var Responce: Users,
    var Response:ArticleResponse,
    var detail : String="",
    var token:String,
    var uidb64:String?=null,
    var message:String = ""

/*{
    "status": "Successfull",
    "Responce": {
        "id": 11,
        "email": "supreeeiyaaaraj@gmail.com",
        "contactno": "212309586",
        "fullname": "supeeraaiya raj sao",
        "location": "bangalore",
        "dob": "1940-08-02",
        "gender": "FEMALE",
        "otp": "449588",
        "is_verified": false
    }
}*/


) : Serializable  {
}