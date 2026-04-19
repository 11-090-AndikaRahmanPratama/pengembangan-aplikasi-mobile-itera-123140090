package com.newsfeed.myprofileapp.data

import android.content.Context
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}

class NewsRepository(private val client: HttpClient, context: Context) {
    private val baseUrl = "https://jsonplaceholder.typicode.com/posts"
    private val prefs = context.getSharedPreferences("news_cache", Context.MODE_PRIVATE)

    suspend fun getNews(): Result<List<News>> {
        return try {
            val response: List<News> = client.get(baseUrl).body()

            prefs.edit().putString("cached_news", Json.encodeToString(response)).apply()

            Result.success(response)
        } catch (e: Exception) {
            val cachedData = prefs.getString("cached_news", null)
            if (cachedData != null) {
                val offlineNews = Json.decodeFromString<List<News>>(cachedData)
                Result.success(offlineNews)
            } else {
                Result.failure(e)
            }
        }
    }
}