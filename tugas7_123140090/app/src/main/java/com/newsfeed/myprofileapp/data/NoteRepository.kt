package com.newsfeed.myprofileapp.data

import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.newsfeed.myprofileapp.db.Note
import com.newsfeed.myprofileapp.db.NotesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class MockRemoteApi {
    suspend fun fetchNotesFromServer() {
        delay(1500)
        Log.d("API_SYNC", "[BONUS] Berhasil narik (GET) data dari server remote!")
    }

    suspend fun createNoteOnServer(title: String) {
        delay(1000)
        Log.d("API_SYNC", "[BONUS] Catatan '$title' berhasil di-push (POST) ke server remote!")
    }
}

class NoteRepository(database: NotesDatabase) {

    private val queries = database.noteQueries
    private val remoteApi = MockRemoteApi()

    fun getAllNotes(): Flow<List<Note>> {
        return queries.selectAll().asFlow().mapToList(Dispatchers.IO)
            .onStart {
                try {
                    remoteApi.fetchNotesFromServer()
                } catch (e: Exception) {
                    Log.e("API_SYNC", "[BONUS] Gagal terhubung ke server, pakai data cache lokal.")
                }
            }
    }

    fun searchNotes(query: String): Flow<List<Note>> {
        return queries.search(query = query).asFlow().mapToList(Dispatchers.IO)
    }

    suspend fun getNoteById(id: Long): Note? {
        return withContext(Dispatchers.IO) {
            queries.selectById(id).executeAsOneOrNull()
        }
    }

    suspend fun insertNote(title: String, content: String) {
        withContext(Dispatchers.IO) {
            val createdAt = System.currentTimeMillis()
            queries.insert(title = title, content = content, created_at = createdAt)

            try {
                remoteApi.createNoteOnServer(title)
            } catch (e: Exception) {
                Log.e("API_SYNC", "[BONUS] Server error, masuk antrean sync lokal.")
            }
        }
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        withContext(Dispatchers.IO) {
            queries.update(title = title, content = content, id = id)
        }
    }

    suspend fun deleteNote(id: Long) {
        withContext(Dispatchers.IO) {
            queries.delete(id = id)
        }
    }
}