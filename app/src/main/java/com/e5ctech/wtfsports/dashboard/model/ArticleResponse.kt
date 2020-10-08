package com.e5ctech.wtfsports.dashboard.model

import com.e5ctech.wtfsports.accounts.models.Sports
import java.io.Serializable

class ArticleResponse : Serializable {

   /* "status": "ok",
    "totalResults": 70,
    "articles":*/

    val status=""
    val totalResults=""
    val articles = mutableListOf<Articles>()
}