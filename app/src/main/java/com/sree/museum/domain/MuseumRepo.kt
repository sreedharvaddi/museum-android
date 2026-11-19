package com.sree.museum.domain

import androidx.paging.PagingData
import com.sree.museum.domain.model.MuseumItem
import kotlinx.coroutines.flow.Flow

interface MuseumRepo {
    suspend fun fetchMuseumItems(query: String, page: Int) : List<MuseumItem>

    fun getMuseumItemsFlow(query: String): Flow<PagingData<MuseumItem>>
}