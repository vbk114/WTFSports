package com.e5ctech.wtfsports.accounts.models

import com.e5ctech.wtfsports.dashboard.model.Articles
import java.io.Serializable

class SportsLeauge : Serializable {

  /*  {
                    "id": 1,
                    "leauge_name": "IPL",
                    "leauge_profile_image": null,
                    "team_leauge_id": [
                        {
                            "id": 1,
                            "team_name": "Royal Challengers Bangalore Squad (RCB)",
                            "team_profile_image": null
                        },
                    ]
                },*/

    val id=0
    val leauge_name=""
    val leauge_profile_image=""
    val team_leauge_id = mutableListOf<TeamLeauge>()
    val isSelected = false;
}