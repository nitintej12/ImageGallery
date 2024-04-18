package com.dol.unsplashapplication.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dol.unsplashapplication.data.model.PhotoItem
import com.dol.unsplashapplication.data.repositories.ApiRepositoryImpl
import com.dol.unsplashapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ListState {
    IDLE,
    LOADING,
    PAGINATING,
    ERROR,
    PAGINATION_EXHAUST,
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiRepository: ApiRepositoryImpl
) : ViewModel() {


    val newsList = mutableStateListOf<PhotoItem>()


    private var page by mutableIntStateOf(1)
    var canPaginate by mutableStateOf(false)
    var listState by mutableStateOf(ListState.IDLE)


    init {
        getPhotos()
    }

    fun getPhotos() {
        if (page == 1 || (page != 1 && canPaginate) && listState == ListState.IDLE) {
            listState = if (page == 1) ListState.LOADING else ListState.PAGINATING

            viewModelScope.launch {
                apiRepository.getPhotos(page).collect() {
                    if (it is NetworkResult.Success) {
                        canPaginate = it.data?.size!! >= 10

                        if (page == 1) {
                            newsList.clear()
                            newsList.addAll(it.data)
                            page++
                        } else {
                            newsList.addAll(it.data)
                        }

                        listState = ListState.IDLE

                        if (canPaginate)
                            page++
                    } else {
                        listState = if (page == 1) ListState.ERROR else ListState.PAGINATION_EXHAUST
                    }
                }
            }
        }
    }

    override fun onCleared() {
        page = 1
        listState = ListState.IDLE
        canPaginate = false
        super.onCleared()

    }
}