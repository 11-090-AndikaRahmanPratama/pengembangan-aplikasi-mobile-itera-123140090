package com.newsfeed.myprofileapp.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.newsfeed.myprofileapp.TestTags
import com.newsfeed.myprofileapp.data.NoteRepositoryInterface
import com.newsfeed.myprofileapp.db.Note
import com.newsfeed.myprofileapp.viewmodel.NotesViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockRepo = mockk<NoteRepositoryInterface>(relaxed = true)

    @Before
    fun setup() {
        every { mockRepo.getAllNotes() } returns flowOf(emptyList())
        every { mockRepo.searchNotes(any()) } returns flowOf(emptyList())
    }

    @Test
    fun emptyState_showsEmptyMessage() {
        val viewModel = NotesViewModel(mockRepo)

        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel)
        }

        composeTestRule
            .onNodeWithTag(TestTags.EMPTY_STATE)
            .assertIsDisplayed()
    }

    @Test
    fun fabButton_isDisplayed() {
        val viewModel = NotesViewModel(mockRepo)

        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel)
        }

        composeTestRule
            .onNodeWithTag(TestTags.FAB_ADD)
            .assertIsDisplayed()
    }

    @Test
    fun searchField_isDisplayedAndAcceptsInput() {
        val viewModel = NotesViewModel(mockRepo)

        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel)
        }

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_FIELD)
            .assertIsDisplayed()
            .performTextInput("cari sesuatu")
    }

    @Test
    fun fabClick_opensAddNoteDialog() {
        val viewModel = NotesViewModel(mockRepo)

        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel)
        }

        composeTestRule
            .onNodeWithTag(TestTags.FAB_ADD)
            .performClick()

        composeTestRule
            .onNodeWithText("Catatan Baru")
            .assertIsDisplayed()
    }
}
