package com.newsfeed.myprofileapp.ai

class AIRepository(private val geminiService: GeminiService) {

    // system prompt yang kita prepend ke pesan pertama
    private val systemContext = buildString {
        append("Kamu adalah asisten pintar di aplikasi MyProfileApp. ")
        append("Aplikasi ini milik Andika Rahman Pratama, mahasiswa Teknik Informatika ITERA (NIM 123140090). ")
        append("Kamu bisa bantu jawab pertanyaan seputar catatan, tugas kuliah, atau hal umum lainnya. ")
        append("Jawab dengan bahasa yang santai tapi tetap informatif. ")
        append("Kalau ditanya soal hal yang kamu ga tau pasti, bilang aja jujur.")
    }

    private var sudahKirimContext = false

    suspend fun chat(pesanUser: String): Result<String> {
        // kalau ini pesan pertama, prepend system context
        val pesanFinal = if (!sudahKirimContext) {
            sudahKirimContext = true
            "$systemContext\n\nPertanyaan user: $pesanUser"
        } else {
            pesanUser
        }
        return geminiService.sendMessage(pesanFinal)
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
