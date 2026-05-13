package com.newsfeed.myprofileapp.data

import com.newsfeed.myprofileapp.db.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepositoryInterface {
    fun getAllNotes(): Flow<List<Note>>
    fun searchNotes(query: String): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(title: String, content: String)
    suspend fun updateNote(id: Long, title: String, content: String)
    suspend fun deleteNote(id: Long)
}
