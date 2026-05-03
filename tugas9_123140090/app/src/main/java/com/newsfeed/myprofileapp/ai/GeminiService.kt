package com.newsfeed.myprofileapp.ai

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.readUTF8Line
import com.newsfeed.myprofileapp.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class GeminiService(private val httpClient: HttpClient) {

    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta"
    private val modelName = "gemini-2.5-flash"

    private val jsonParser = Json { ignoreUnknownKeys = true; isLenient = true }

    // riwayat percakapan disimpan di sini supaya multi-turn jalan
    private val chatHistory = mutableListOf<GeminiContent>()

    suspend fun sendMessage(userText: String): Result<String> {
        return try {
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

            val response: GeminiResponse = httpClient.post(
                "$baseUrl/models/$modelName:generateContent"
            ) {
                contentType(ContentType.Application.Json)
                parameter("key", BuildConfig.GEMINI_API_KEY)
                setBody(requestBody)
            }.body()

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

    // streaming response - teks muncul bertahap
    fun streamMessage(userText: String): Flow<String> = flow {
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

        try {
            val fullResponse = StringBuilder()

            httpClient.preparePost(
                "$baseUrl/models/$modelName:streamGenerateContent"
            ) {
                contentType(ContentType.Application.Json)
                parameter("key", BuildConfig.GEMINI_API_KEY)
                parameter("alt", "sse")
                setBody(requestBody)
            }.execute { response ->
                val channel = response.bodyAsChannel()
                while (!channel.isClosedForRead) {
                    val line = channel.readUTF8Line() ?: continue
                    if (!line.startsWith("data: ")) continue

                    val jsonStr = line.removePrefix("data: ").trim()
                    if (jsonStr.isEmpty()) continue

                    try {
                        val chunk = jsonParser.decodeFromString<GeminiResponse>(jsonStr)
                        val partText = chunk.candidates
                            ?.firstOrNull()
                            ?.content
                            ?.parts
                            ?.firstOrNull()
                            ?.text

                        if (partText != null) {
                            fullResponse.append(partText)
                            emit(fullResponse.toString())
                        }
                    } catch (_: Exception) {
                        // skip chunk yang ga bisa di-parse
                    }
                }
            }

            val finalText = fullResponse.toString().ifEmpty { "Tidak ada respons dari AI." }
            chatHistory.add(
                GeminiContent(
                    parts = listOf(GeminiPart(text = finalText)),
                    role = "model"
                )
            )
        } catch (e: Exception) {
            chatHistory.removeLastOrNull()
            Log.e("GeminiService", "Stream error: ${e.message}")
            throw e
        }
    }

    // kirim gambar + teks ke gemini (multimodal)
    suspend fun analyzeImage(base64Image: String, mimeType: String, prompt: String): Result<String> {
        return try {
            val parts = listOf(
                GeminiPart(text = prompt),
                GeminiPart(
                    inlineData = InlineData(
                        mimeType = mimeType,
                        data = base64Image
                    )
                )
            )

            val requestBody = GeminiRequest(
                contents = listOf(
                    GeminiContent(parts = parts, role = "user")
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.5,
                    maxOutputTokens = 1024
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
                ?: "Tidak bisa menganalisis gambar."

            Result.success(resultText)
        } catch (e: Exception) {
            Log.e("GeminiService", "Image analysis error: ${e.message}")
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
