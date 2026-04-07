package com.newsfeed.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.newsfeed.myprofileapp.data.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun updateProfile(newName: String, newBio: String) {
        _uiState.update { currentState ->
            currentState.copy(name = newName, bio = newBio)
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isDarkMode = isDark)
        }
    }
}