package com.dol.unsplashapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dol.unsplashapplication.data.model.ResultItem
import com.dol.unsplashapplication.data.repositories.ApiRepositoryImpl
import com.dol.unsplashapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeState(
    val isLoading: Boolean = false,
    val photosResult: List<ResultItem>? = null,
    val isError: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiRepository: ApiRepositoryImpl
) : ViewModel() {

    private val _photos = MutableStateFlow(HomeState())
    val photos: StateFlow<HomeState> = _photos.asStateFlow()


    init {
        getPhotos()
    }

    fun getPhotos() {
        viewModelScope.launch {
            _photos.emit(
                HomeState().copy(
                    isLoading = true
                )
            )
            apiRepository.getPhotos().collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _photos.emit(
                            HomeState().copy(
                                isLoading = false, photosResult = result.data, isError = false
                            )
                        )
                    }

                    is NetworkResult.Error -> {
                        _photos.emit(
                            HomeState().copy(
                                isLoading = false,
                                isError = true
                            )
                        )
                    }

                    is NetworkResult.Loading -> {
                        _photos.emit(
                            HomeState().copy(
                                isLoading = true,
                                isError = false,
                                photosResult = null
                            )
                        )
                    }
                }
            }
        }
    }
}