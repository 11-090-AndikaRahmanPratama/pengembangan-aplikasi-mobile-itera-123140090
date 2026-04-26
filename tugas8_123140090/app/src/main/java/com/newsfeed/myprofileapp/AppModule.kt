package com.newsfeed.myprofileapp

import com.newsfeed.myprofileapp.data.NoteRepository
import com.newsfeed.myprofileapp.data.SettingsManager
import com.newsfeed.myprofileapp.viewmodel.NotesViewModel
import com.newsfeed.myprofileapp.viewmodel.ProfileViewModel
import com.newsfeed.myprofileapp.data.DatabaseProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // 1. Platform APIs (Sebutin tipenya eksplisit biar gak "Cannot Infer")
    single<DeviceInfo> { DeviceInfoImpl() }
    single<BatteryInfo> { BatteryInfoImpl(androidContext()) }
    single<NetworkMonitor> { NetworkMonitorImpl(androidContext()) }

    // 2. Data
    single { SettingsManager(androidContext()) }
    single { DatabaseProvider.getDatabase(androidContext()) }
    single { NoteRepository(get()) }

    // 3. ViewModels
    viewModel { NotesViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
}