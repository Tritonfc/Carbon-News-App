package com.tobe.carbonnewsapp.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity(
    tableName = "articles",

            indices = [Index(value = ["url",],
        unique = true)]
)
@Serializable
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val author: String? = null,
    val content: String? = null,         // Make content nullable
    val description: String? = null,     // Make description nullable
    val publishedAt: String? = null,
    val source: Source,
    val title: String,
    val url: String,                     // Assuming url is always present
    val urlToImage: String? = null
)