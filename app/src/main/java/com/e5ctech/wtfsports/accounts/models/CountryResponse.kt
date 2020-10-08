package bibou.biboubeauty.com.utils.networking

import com.e5ctech.wtfsports.accounts.models.Countries
import com.e5ctech.wtfsports.accounts.models.Sports
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.dashboard.model.ArticleResponse
import java.io.Serializable

class CountryResponse() : Serializable  {

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


    var status: String =""
    lateinit var Responce:MutableList<Countries>
    var detail : String=""
}