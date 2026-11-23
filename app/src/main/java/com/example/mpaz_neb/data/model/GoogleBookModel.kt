package com.example.mpaz_neb.data.model

import com.google.gson.annotations.SerializedName

data class GoogleBooksResponse(
    val items: List<GoogleBookItem>? // Puede venir nulo si no hay resultados
)

data class GoogleBookItem(
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    @SerializedName("imageLinks") val imageLinks: ImageLinks?
)

data class ImageLinks(
    @SerializedName("thumbnail") val thumbnail: String?
)