package com.newsfeed.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.newsfeed.myprofileapp.BatteryInfo
import com.newsfeed.myprofileapp.DeviceInfo
import com.newsfeed.myprofileapp.NetworkMonitor
import com.newsfeed.myprofileapp.data.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    // Tambahin sensor di constructor buat Koin
    private val deviceInfo: DeviceInfo,
    private val batteryInfo: BatteryInfo,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Fungsi pembantu buat ambil data sensor
    fun getDeviceModel() = deviceInfo.getInfo()
    fun getBatteryLevel() = batteryInfo.getLevel()
    val isOnline: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun updateProfile(newName: String, newBio: String) {
        _uiState.update { currentState ->
            currentState.copy(name = newName, bio = newBio)
        }
    }
}