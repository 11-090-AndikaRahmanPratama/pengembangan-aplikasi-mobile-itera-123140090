# Tugas 9 - Integrasi AI API
**Nama:** Andika Rahman Pratama  
**NIM:** 123140090  
**Prodi:** Teknik Informatika - ITERA

## Deskripsi
Aplikasi ini merupakan pengembangan dari Tugas 8 dengan penambahan fitur **AI Chat Assistant** menggunakan Google Gemini API. Tab baru ditambahkan di bottom navigation untuk fitur AI Chat, sementara semua fitur dari tugas sebelumnya tetap dipertahankan.

### Fitur Baru Tugas 9:
1. **AI Chat Assistant:** Tab baru berisi chatbot berbasis Gemini 2.5 Flash. User bisa mengetik pertanyaan dan mendapat jawaban dari AI secara langsung di dalam aplikasi.
2. **Multi-turn Conversation (Bonus +5%):** Riwayat percakapan disimpan selama sesi, sehingga AI bisa mengingat konteks percakapan sebelumnya. User juga bisa menghapus riwayat chat lewat tombol hapus di pojok kanan atas.
3. **Error Handling:** Penanganan error untuk berbagai kasus: API key tidak valid (401), rate limit tercapai (429), tidak ada koneksi internet, dan error lainnya. Error ditampilkan lewat Snackbar.
4. **Loading States:** Animasi typing indicator berupa tiga titik yang berkedip bergantian saat AI sedang memproses jawaban.
5. **System Prompt:** AI dikonfigurasi dengan system prompt yang mengarahkannya sebagai asisten aplikasi MyProfileApp milik mahasiswa Teknik Informatika ITERA.

### Tech Stack:
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose + Material 3
- **AI API:** Google Gemini 2.5 Flash
- **HTTP Client:** Ktor Client
- **DI Framework:** Koin
- **Serialization:** kotlinx.serialization
- **Async:** Kotlin Coroutines & Flow
- **Database:** SQLDelight
- **Preferences:** DataStore

### Struktur File Baru (Tugas 9):
```
ai/
├── GeminiModels.kt      # Data class untuk request & response Gemini API
├── GeminiService.kt     # Service HTTP ke Gemini API (multi-turn)
└── AIRepository.kt      # Repository layer dengan system prompt
viewmodel/
└── ChatViewModel.kt     # State management untuk halaman chat
screens/
└── ChatScreen.kt        # UI halaman chat (bubble, typing indicator)
```

### Cara Setup API Key:
1. Buka [Google AI Studio](https://aistudio.google.com)
2. Klik **Get API Key** lalu **Create API Key**
3. Tambahkan baris berikut di file `local.properties`:
   ```
   GEMINI_API_KEY=your_api_key_here
   ```
4. Rebuild project


## Showcase

### Fitur AI Chat (Tugas 9)
| Empty State | Percakapan dengan AI | Typing Indicator |
| :---: | :---: | :---: |
| ![Empty](./empty.png) | ![Chat](./conversation.png) | ![Loading](./loading.png) |