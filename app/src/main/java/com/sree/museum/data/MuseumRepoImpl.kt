package com.sree.museum.data

import android.util.Log
import com.sree.museum.data.api.MuseumApi
import com.sree.museum.data.api.MuseumItemSearchApi
import com.sree.museum.data.api.model.toDomain
import com.sree.museum.domain.MuseumRepo
import com.sree.museum.domain.model.MuseumItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MuseumRepoImpl @Inject constructor(
    private val museumApi: MuseumApi,
    private val museumItemSearchApi: MuseumItemSearchApi
) : MuseumRepo {

    private val TAG = "MuseumRepoImpl"
    override suspend fun fetchMuseumItems(query: String, page: Int): List<MuseumItem> {
        return museumItemSearchApi.search(query, offset = page, limit = 20).objectIDs.map { objectId ->
            Log.d(TAG, "Fetching item with ID: $objectId")
            val museumItem = museumApi.getMuseumItemById(objectId).toDomain()
            Log.d(TAG, "Fetched item: ${museumItem.thumbnailUrl}")
            museumItem
        }.filter { it.thumbnailUrl.isNotEmpty() }
    }

    private fun stubData() : List<MuseumItem> {
        return listOf(
            MuseumItem(
                id = 1,
                primaryImageUrl =  "https://images.metmuseum.org/CRDImages/ad/original/DP208427.jpg",
                thumbnailUrl = "https://images.metmuseum.org/CRDImages/ad/web-large/DP208427.jpg",
                dated = "1976",
                location = "New York City",
                ""
            ),
            MuseumItem(
                id = 2,
                primaryImageUrl =  "https://images.metmuseum.org/CRDImages/ad/original/DP208427.jpg",
                thumbnailUrl = "https://images.metmuseum.org/CRDImages/ad/web-large/DP208427.jpg",
                dated = "1976",
                location = "New York City",
                ""
            ),
            MuseumItem(
                id = 3,
                primaryImageUrl =  "https://images.metmuseum.org/CRDImages/ad/original/DP208427.jpg",
                thumbnailUrl = "https://images.metmuseum.org/CRDImages/ad/web-large/DP208427.jpg",
                dated = "1976",
                location = "New York City",
                ""
            ),
            MuseumItem(
                id = 13456,
                primaryImageUrl =  "https://images.metmuseum.org/CRDImages/ad/original/DP208427.jpg",
                thumbnailUrl = "https://images.metmuseum.org/CRDImages/ad/web-large/DP208427.jpg",
                dated = "1976",
                location = "New York City",
                ""
            ),
            MuseumItem(
                id = 13456,
                primaryImageUrl =  "https://images.metmuseum.org/CRDImages/ad/original/DP208427.jpg",
                thumbnailUrl = "https://images.metmuseum.org/CRDImages/ad/web-large/DP208427.jpg",
                dated = "1976",
                location = "New York City",
                ""
            ),
            MuseumItem(
                id = 13456,
                primaryImageUrl =  "https://images.metmuseum.org/CRDImages/ad/original/DP208427.jpg",
                thumbnailUrl = "https://images.metmuseum.org/CRDImages/ad/web-large/DP208427.jpg",
                dated = "1976",
                location = "New York City",
                ""
            ),
        )
    }
}
