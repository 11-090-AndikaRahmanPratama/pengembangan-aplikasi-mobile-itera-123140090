package com.newsfeed.myprofileapp.ai

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.newsfeed.myprofileapp.BuildConfig

class GeminiService(private val httpClient: HttpClient) {

    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta"
    private val modelName = "gemini-2.5-flash"

    // riwayat percakapan disimpan di sini supaya multi-turn jalan
    private val chatHistory = mutableListOf<GeminiContent>()

    suspend fun sendMessage(userText: String): Result<String> {
        return try {
            // masukin pesan user ke history
            chatHistory.add(
                GeminiContent(
                    parts = listOf(GeminiPart(text = userText)),
                    role = "user"
                )
            )

            val requestBody = GeminiRequest(
                contents = chatHistory.toList(),
                generationConfig = GenerationConfig(
                    temperature = 0.7,
                    maxOutputTokens = 1024
                )
            )

            val apiKey = BuildConfig.GEMINI_API_KEY

            val response: GeminiResponse = httpClient.post(
                "$baseUrl/models/$modelName:generateContent"
            ) {
                contentType(ContentType.Application.Json)
                parameter("key", apiKey)
                setBody(requestBody)
            }.body()

            // cek apakah ada error dari API
            if (response.error != null) {
                chatHistory.removeLastOrNull()
                return Result.failure(
                    Exception("Gemini Error ${response.error.code}: ${response.error.message}")
                )
            }

            val resultText = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?: "Tidak ada respons dari AI."

            // simpan jawaban AI ke history juga
            chatHistory.add(
                GeminiContent(
                    parts = listOf(GeminiPart(text = resultText)),
                    role = "model"
                )
            )

            Result.success(resultText)
        } catch (e: Exception) {
            chatHistory.removeLastOrNull()
            Log.e("GeminiService", "Gagal kirim pesan: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun generateSingle(prompt: String): Result<String> {
        return try {
            val requestBody = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = prompt)),
                        role = "user"
                    )
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.5,
                    maxOutputTokens = 512
                )
            )

            val response: GeminiResponse = httpClient.post(
                "$baseUrl/models/$modelName:generateContent"
            ) {
                contentType(ContentType.Application.Json)
                parameter("key", BuildConfig.GEMINI_API_KEY)
                setBody(requestBody)
            }.body()

            val resultText = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?: "Tidak ada respons."

            Result.success(resultText)
        } catch (e: Exception) {
            Log.e("GeminiService", "generateSingle error: ${e.message}")
            Result.failure(e)
        }
    }

    fun resetChat() {
        chatHistory.clear()
    }
}
