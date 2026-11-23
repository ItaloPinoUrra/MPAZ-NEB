package com.example.mpaz_neb.data.model

import com.google.gson.annotations.SerializedName


data class OpenLibraryResponse(
    val docs: List<Book>
)

data class Book(
    val title: String,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?,
    @SerializedName("cover_i") val coverId: Int?
)