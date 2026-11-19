package com.sree.museum.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sree.museum.data.api.MuseumApi
import com.sree.museum.data.api.MuseumItemSearchApi
import com.sree.museum.data.api.model.toDomain
import com.sree.museum.domain.MuseumRepo
import com.sree.museum.domain.model.MuseumItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MuseumRepoImpl @Inject constructor(
    private val museumApi: MuseumApi,
    private val museumItemSearchApi: MuseumItemSearchApi
) : MuseumRepo {

    private val TAG = "MuseumRepoImpl"
    override suspend fun fetchMuseumItems(query: String, page: Int): List<MuseumItem> {
        val offset = (page - 1) * 20
        // Deduplicate objectIDs to prevent duplicate keys in Compose LazyList
        val uniqueIds = museumItemSearchApi.search(query, offset = offset, limit = 20).objectIDs.distinct()
        return uniqueIds.mapNotNull { objectId ->
            Log.d(TAG, "Fetching item with ID: $objectId")
            try {
                val museumItem = museumApi.getMuseumItemById(objectId).toDomain()
                Log.d(TAG, "Fetched item: ${museumItem.thumbnailUrl}")
                museumItem
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching item with ID: $objectId", e)
                null
            }

        }.filter { it.thumbnailUrl.isNotEmpty() }
    }

    override fun getMuseumItemsFlow(query: String): Flow<PagingData<MuseumItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { MuseumPagingSource(this, query = query) }
        ).flow
    }


}
