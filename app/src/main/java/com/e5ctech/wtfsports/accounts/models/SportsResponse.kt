package bibou.biboubeauty.com.utils.networking

import com.e5ctech.wtfsports.accounts.models.Sports
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.dashboard.model.ArticleResponse
import java.io.Serializable

class SportsResponse() : Serializable  {

    /*{
    "status": "Successfull",
     "Response": {
        "sports": [
            {
                "sportsname": "Cricket",
                "league_sport_id": [
}*/


    var status: String =""
    lateinit var sports:MutableList<Sports>
    var detail : String=""
}