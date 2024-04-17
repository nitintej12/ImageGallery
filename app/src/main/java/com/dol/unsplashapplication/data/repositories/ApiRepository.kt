package com.dol.unsplashapplication.data.repositories

import com.dol.unsplashapplication.data.model.ResultItem
import com.dol.unsplashapplication.utils.BaseApiResponse
import com.dol.unsplashapplication.data.network.ApiService
import com.dol.unsplashapplication.utils.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class ApiRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    BaseApiResponse() {
    suspend fun getPhotos(): Flow<NetworkResult<List<ResultItem>>> {
        return flow {
            emit(safeApiCall { apiService.getPhotos() })
        }.flowOn(Dispatchers.IO)
    }
}