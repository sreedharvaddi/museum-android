package com.sree.museum.data.api

import com.sree.museum.data.api.model.MuseumItemResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface MuseumApi {
    @GET("v1/objects/{id}")
    suspend fun getMuseumItemById(@Path("id") id: Int): MuseumItemResponse

    @GET("v1/objects")
    suspend fun getMuseumItems(): List<String>
}