package com.newsfeed.myprofileapp.data

import com.newsfeed.myprofileapp.db.Note
import com.newsfeed.myprofileapp.db.NoteQueries
import com.newsfeed.myprofileapp.db.NotesDatabase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class NoteRepositoryTest {

    private val mockDatabase = mockk<NotesDatabase>()
    private val mockQueries = mockk<NoteQueries>(relaxed = true)
    private lateinit var repository: NoteRepository

    @Before
    fun setup() {
        every { mockDatabase.noteQueries } returns mockQueries

        mockkStatic(android.util.Log::class)
        every { android.util.Log.d(any(), any()) } returns 0
        every { android.util.Log.e(any(), any()) } returns 0

        repository = NoteRepository(mockDatabase)
    }

    @Test
    fun `getAllNotes returns flow from database`() {
        val flow = repository.getAllNotes()
        assertNotNull(flow)
    }

    @Test
    fun `searchNotes returns flow from database`() {
        val flow = repository.searchNotes("test")
        assertNotNull(flow)
    }

    @Test
    fun `insertNote calls database insert`() = runTest {
        repository.insertNote("Judul Test", "Konten test")
        verify { mockQueries.insert(title = "Judul Test", content = "Konten test", created_at = any()) }
    }

    @Test
    fun `updateNote calls database update`() = runTest {
        repository.updateNote(1L, "Judul Baru", "Konten baru")
        verify { mockQueries.update(title = "Judul Baru", content = "Konten baru", id = 1L) }
    }

    @Test
    fun `deleteNote calls database delete`() = runTest {
        repository.deleteNote(42L)
        verify { mockQueries.delete(42L) }
    }

    @Test
    fun `getNoteById calls database selectById`() = runTest {
        every { mockQueries.selectById(5L).executeAsOneOrNull() } returns
                Note(id = 5L, title = "Catatan 5", content = "Isi", created_at = 0L)

        val result = repository.getNoteById(5L)
        assertNotNull(result)
        verify { mockQueries.selectById(5L) }
    }
}
