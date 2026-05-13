package com.newsfeed.myprofileapp.ai

import kotlinx.coroutines.flow.Flow

class AIRepository(private val geminiService: GeminiService) {

    private val systemContext = buildString {
        append("Kamu adalah asisten pintar di aplikasi MyProfileApp. ")
        append("Aplikasi ini milik Andika Rahman Pratama, mahasiswa Teknik Informatika ITERA (NIM 123140090). ")
        append("Kamu bisa bantu jawab pertanyaan seputar catatan, tugas kuliah, atau hal umum lainnya. ")
        append("Jawab dengan bahasa yang santai tapi tetap informatif. ")
        append("Kalau ditanya soal hal yang kamu ga tau pasti, bilang aja jujur.")
    }

    private var sudahKirimContext = false

    suspend fun chat(pesanUser: String): Result<String> {
        val pesanFinal = if (!sudahKirimContext) {
            sudahKirimContext = true
            "$systemContext\n\nPertanyaan user: $pesanUser"
        } else {
            pesanUser
        }
        return geminiService.sendMessage(pesanFinal)
    }

    // streaming - teks muncul bertahap di UI
    fun chatStream(pesanUser: String): Flow<String> {
        val pesanFinal = if (!sudahKirimContext) {
            sudahKirimContext = true
            "$systemContext\n\nPertanyaan user: $pesanUser"
        } else {
            pesanUser
        }
        return geminiService.streamMessage(pesanFinal)
    }

    // analisis gambar pakai Gemini multimodal
    suspend fun analisisGambar(base64Image: String, mimeType: String): Result<String> {
        val prompt = buildString {
            append("Analisis gambar berikut ini. ")
            append("Jelaskan apa yang kamu lihat di gambar tersebut. ")
            append("Jawab dalam bahasa Indonesia yang ringkas dan informatif.")
        }
        return geminiService.analyzeImage(base64Image, mimeType, prompt)
    }

    suspend fun rangkumCatatan(judul: String, isi: String): Result<String> {
        val prompt = buildString {
            append("Tolong rangkum catatan berikut ini dalam 2-3 kalimat. ")
            append("Pakai bahasa Indonesia yang ringkas.\n\n")
            append("Judul: $judul\n")
            append("Isi:\n$isi")
        }
        return geminiService.generateSingle(prompt)
    }

    fun resetPercakapan() {
        sudahKirimContext = false
        geminiService.resetChat()
    }
}
