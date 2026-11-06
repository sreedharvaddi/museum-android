package com.sree.museum.domain

import com.sree.museum.domain.model.MuseumItem

interface MuseumRepo {
    suspend fun fetchMuseumItems(query: String, page: Int) : List<MuseumItem>
}