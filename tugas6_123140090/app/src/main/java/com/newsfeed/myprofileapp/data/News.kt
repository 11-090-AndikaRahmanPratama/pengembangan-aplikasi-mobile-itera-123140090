package com.newsfeed.myprofileapp.data

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val id: Int,
    val userId: Int? = null,
    val title: String,
    val body: String
) {
    val imageUrl: String
        get() = "https://picsum.photos/seed/$id/400/200"
}