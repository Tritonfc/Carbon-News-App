package com.tobe.carbonnewsapp.data.remoteDataSource.api

import com.tobe.carbonnewsapp.data.models.Article


data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)