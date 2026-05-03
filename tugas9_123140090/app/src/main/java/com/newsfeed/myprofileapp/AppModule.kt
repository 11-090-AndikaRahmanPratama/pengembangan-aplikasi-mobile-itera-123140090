package com.newsfeed.myprofileapp

import com.newsfeed.myprofileapp.ai.AIRepository
import com.newsfeed.myprofileapp.ai.GeminiService
import com.newsfeed.myprofileapp.data.NoteRepository
import com.newsfeed.myprofileapp.data.SettingsManager
import com.newsfeed.myprofileapp.viewmodel.ChatViewModel
import com.newsfeed.myprofileapp.viewmodel.NotesViewModel
import com.newsfeed.myprofileapp.viewmodel.ProfileViewModel
import com.newsfeed.myprofileapp.data.DatabaseProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.HttpTimeout
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<DeviceInfo> { DeviceInfoImpl() }
    single<BatteryInfo> { BatteryInfoImpl(androidContext()) }
    single<NetworkMonitor> { NetworkMonitorImpl(androidContext()) }

    single { SettingsManager(androidContext()) }
    single { DatabaseProvider.getDatabase(androidContext()) }
    single { NoteRepository(get()) }

    // HTTP client untuk Gemini API
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = false
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 30_000
            }
        }
    }

    // AI layer
    single { GeminiService(get()) }
    single { AIRepository(get()) }

    viewModel { NotesViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { ChatViewModel(get()) }
}