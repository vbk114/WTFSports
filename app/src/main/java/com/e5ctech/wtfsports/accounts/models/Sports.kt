package com.e5ctech.wtfsports.accounts.models

import com.e5ctech.wtfsports.dashboard.model.Articles
import java.io.Serializable

class Sports : Serializable {

   /* "sportsname": 70,
    "league_sport_id":*/

    val sportsname=""
    val league_sport_id = mutableListOf<SportsLeauge>()
}