package com.newsfeed.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsfeed.myprofileapp.data.News
import com.newsfeed.myprofileapp.data.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val newsList: List<News>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading

            repository.getNews().fold(
                onSuccess = { newsList ->
                    _uiState.value = NewsUiState.Success(newsList)
                },
                onFailure = { error ->
                    _uiState.value = NewsUiState.Error(error.localizedMessage ?: "Terjadi kesalahan tak terduga")
                }
            )
        }
    }
    fun getNewsById(id: Int): News? {
        val currentState = _uiState.value
        if (currentState is NewsUiState.Success) {
            return currentState.newsList.find { it.id == id }
        }
        return null
    }
}