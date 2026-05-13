package com.newsfeed.myprofileapp.viewmodel

import com.newsfeed.myprofileapp.data.NoteRepositoryInterface
import com.newsfeed.myprofileapp.db.Note
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.Runs
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockRepo = mockk<NoteRepositoryInterface>(relaxed = true)
    private lateinit var viewModel: NotesViewModel

    private val sampleNotes = listOf(
        Note(id = 1, title = "Catatan 1", content = "Isi pertama", created_at = 1000L),
        Note(id = 2, title = "Catatan 2", content = "Isi kedua", created_at = 2000L)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { mockRepo.getAllNotes() } returns flowOf(sampleNotes)
        every { mockRepo.searchNotes(any()) } returns flowOf(emptyList())
        viewModel = NotesViewModel(mockRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial search query is empty string`() {
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `updateSearchQuery changes search state`() {
        viewModel.updateSearchQuery("belajar")
        assertEquals("belajar", viewModel.searchQuery.value)
    }

    @Test
    fun `addNote calls repository insertNote`() = runTest {
        coEvery { mockRepo.insertNote(any(), any()) } just Runs

        viewModel.addNote("Judul Baru", "Isi baru")
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepo.insertNote("Judul Baru", "Isi baru") }
    }

    @Test
    fun `deleteNote calls repository deleteNote`() = runTest {
        coEvery { mockRepo.deleteNote(any()) } just Runs

        viewModel.deleteNote(1L)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepo.deleteNote(1L) }
    }

    @Test
    fun `updateNote calls repository updateNote`() = runTest {
        coEvery { mockRepo.updateNote(any(), any(), any()) } just Runs

        viewModel.updateNote(1L, "Judul Edit", "Isi edit")
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepo.updateNote(1L, "Judul Edit", "Isi edit") }
    }
}
