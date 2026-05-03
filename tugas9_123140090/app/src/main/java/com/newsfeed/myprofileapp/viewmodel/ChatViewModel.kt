package com.newsfeed.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsfeed.myprofileapp.ai.AIRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
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

    fun kirimPesan(pesan: String) {
        if (pesan.isBlank()) return

        // tambahin pesan user ke list
        _uiState.update { state ->
            state.copy(
                messages = state.messages + ChatMessage(text = pesan, isUser = true),
                sedangLoading = true,
                pesanError = null
            )
        }

        viewModelScope.launch {
            val hasil = aiRepository.chat(pesan)

            hasil.onSuccess { jawaban ->
                _uiState.update { state ->
                    state.copy(
                        messages = state.messages + ChatMessage(text = jawaban, isUser = false),
                        sedangLoading = false
                    )
                }
            }.onFailure { error ->
                val pesanErr = when {
                    error.message?.contains("401") == true -> "API key tidak valid. Cek konfigurasi."
                    error.message?.contains("429") == true -> "Terlalu banyak request. Tunggu sebentar ya."
                    error.message?.contains("Unable to resolve host") == true -> "Tidak ada koneksi internet."
                    else -> error.message ?: "Terjadi kesalahan."
                }
                _uiState.update { state ->
                    state.copy(
                        sedangLoading = false,
                        pesanError = pesanErr
                    )
                }
            }
        }
    }

    fun rangkumCatatan(judul: String, isi: String) {
        _uiState.update { state ->
            state.copy(
                messages = state.messages + ChatMessage(
                    text = "Rangkumkan catatan \"$judul\"",
                    isUser = true
                ),
                sedangLoading = true,
                pesanError = null
            )
        }

        viewModelScope.launch {
            val hasil = aiRepository.rangkumCatatan(judul, isi)

            hasil.onSuccess { rangkuman ->
                _uiState.update { state ->
                    state.copy(
                        messages = state.messages + ChatMessage(text = rangkuman, isUser = false),
                        sedangLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    state.copy(
                        sedangLoading = false,
                        pesanError = error.message ?: "Gagal merangkum catatan."
                    )
                }
            }
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
