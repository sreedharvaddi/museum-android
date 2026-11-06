package com.sree.museum.domain.model

data class MuseumItem(
    val id: Int,
    val primaryImageUrl: String,
    val thumbnailUrl: String,
    val dated: String,
    val location: String,
    val description: String
)
