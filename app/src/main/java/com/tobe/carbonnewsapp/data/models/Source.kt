package com.tobe.carbonnewsapp.data.models

import kotlinx.serialization.Serializable


@Serializable
data class Source(
    val id: String?,
    val name: String?
)