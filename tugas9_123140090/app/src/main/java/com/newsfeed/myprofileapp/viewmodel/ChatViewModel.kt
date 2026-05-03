package com.newsfeed.myprofileapp.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsfeed.myprofileapp.ai.AIRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val imageUri: Uri? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val sedangLoading: Boolean = false,
    val pesanError: String? = null
)

class ChatViewModel(private val aiRepository: AIRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    // kirim pesan teks biasa (pakai streaming supaya teks muncul bertahap)
    fun kirimPesan(pesan: String) {
        if (pesan.isBlank()) return

        _uiState.update { state ->
            state.copy(
                messages = state.messages + ChatMessage(text = pesan, isUser = true),
                sedangLoading = true,
                pesanError = null
            )
        }

        viewModelScope.launch {
            // tambahin placeholder buat jawaban AI yang akan diisi bertahap
            _uiState.update { state ->
                state.copy(
                    messages = state.messages + ChatMessage(text = "", isUser = false)
                )
            }

            aiRepository.chatStream(pesan)
                .catch { error ->
                    // kalau streaming gagal, hapus placeholder dan tampilkan error
                    _uiState.update { state ->
                        val msgs = state.messages.toMutableList()
                        if (msgs.isNotEmpty() && !msgs.last().isUser) {
                            msgs.removeAt(msgs.lastIndex)
                        }
                        state.copy(
                            messages = msgs,
                            sedangLoading = false,
                            pesanError = mapError(error)
                        )
                    }
                }
                .collect { partialText ->
                    // update pesan AI terakhir dengan teks yang masuk bertahap
                    _uiState.update { state ->
                        val msgs = state.messages.toMutableList()
                        if (msgs.isNotEmpty() && !msgs.last().isUser) {
                            msgs[msgs.lastIndex] = msgs.last().copy(text = partialText)
                        }
                        state.copy(messages = msgs)
                    }
                }

            _uiState.update { it.copy(sedangLoading = false) }
        }
    }

    // kirim gambar untuk dianalisis AI
    fun kirimGambar(context: Context, imageUri: Uri) {
        _uiState.update { state ->
            state.copy(
                messages = state.messages + ChatMessage(
                    text = "📷 Analisis gambar ini",
                    isUser = true,
                    imageUri = imageUri
                ),
                sedangLoading = true,
                pesanError = null
            )
        }

        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val bytes = inputStream?.readBytes() ?: throw Exception("Gagal baca gambar")
                inputStream.close()

                val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)

                // deteksi mime type
                val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"

                val hasil = aiRepository.analisisGambar(base64, mimeType)

                hasil.onSuccess { deskripsi ->
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages + ChatMessage(text = deskripsi, isUser = false),
                            sedangLoading = false
                        )
                    }
                }.onFailure { error ->
                    _uiState.update { state ->
                        state.copy(
                            sedangLoading = false,
                            pesanError = mapError(error)
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        sedangLoading = false,
                        pesanError = e.message ?: "Gagal memproses gambar"
                    )
                }
            }
        }
    }

    private fun mapError(error: Throwable): String {
        return when {
            error.message?.contains("401") == true -> "API key tidak valid. Cek konfigurasi."
            error.message?.contains("429") == true -> "Terlalu banyak request. Tunggu sebentar ya."
            error.message?.contains("Unable to resolve host") == true -> "Tidak ada koneksi internet."
            else -> error.message ?: "Terjadi kesalahan."
        }
    }

    fun hapusError() {
        _uiState.update { it.copy(pesanError = null) }
    }

    fun resetChat() {
        aiRepository.resetPercakapan()
        _uiState.value = ChatUiState()
    }
}
