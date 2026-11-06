package com.sree.museum.data.api

import com.sree.museum.data.api.model.MuseumItemIdsReponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MuseumItemSearchApi {

    /** Search the collection by query string */
    @GET("v1.1/search")
    suspend fun search(
        @Query("q") q: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): MuseumItemIdsReponse
}