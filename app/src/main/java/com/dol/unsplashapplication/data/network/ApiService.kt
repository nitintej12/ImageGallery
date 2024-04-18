package com.dol.unsplashapplication.data.network

import com.dol.unsplashapplication.data.model.PhotoItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int
    ): Response<List<PhotoItem>>
}