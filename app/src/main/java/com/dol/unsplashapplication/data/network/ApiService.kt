package com.dol.unsplashapplication.data.network

import com.dol.unsplashapplication.data.model.ResultItem
import com.dol.unsplashapplication.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("photos")
    suspend fun getPhotos(
        @Query("client") id: String = Constants.UNSPLASH_ACCESS_KEY
    ): Response<List<ResultItem>>
}