package com.tobe.carbonnewsapp.navigation


import com.tobe.carbonnewsapp.data.models.Article
import kotlinx.serialization.Serializable


@Serializable
object News

@Serializable
object Saved


@Serializable
data class ViewArticle(val article: String)

