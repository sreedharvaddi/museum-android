package com.sree.museum.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sree.museum.domain.MuseumRepo
import com.sree.museum.domain.model.MuseumItem

class MuseumPagingSource(
    private val museumRepo: MuseumRepo,
    private val query: String
) : PagingSource<Int, MuseumItem>() {

    // Track seen item IDs to prevent duplicates across pages (API may return overlapping results)
    private val _seenKeys = mutableSetOf<Int>()

    override fun getRefreshKey(state: PagingState<Int, MuseumItem>): Int? {
        _seenKeys.clear()
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MuseumItem> {
        val page = params.key ?: 1
        return try {
            val items = museumRepo.fetchMuseumItems(query, page = page)
            // Deduplicate by ID to prevent duplicate keys when Compose renders the list
            val uniqueItems = items.filter { _seenKeys.add(it.id) }
            LoadResult.Page(
                data = uniqueItems,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (uniqueItems.isEmpty())  null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(
                e
            )
        }
    }
}
