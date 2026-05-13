package com.newsfeed.myprofileapp.viewmodel

import app.cash.turbine.test
import com.newsfeed.myprofileapp.ai.AIRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelFlowTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockAiRepo = mockk<AIRepository>(relaxed = true)
    private lateinit var viewModel: ChatViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ChatViewModel(mockAiRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState has empty messages and not loading`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.messages.isEmpty())
            assertFalse(state.sedangLoading)
            assertEquals(null, state.pesanError)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `kirimPesan adds user message to state`() = runTest {
        every { mockAiRepo.chatStream(any()) } returns flowOf("jawaban AI")

        viewModel.uiState.test {
            awaitItem() // initial state

            viewModel.kirimPesan("halo")
            testDispatcher.scheduler.advanceUntilIdle()

            // kumpulkan state sampai dapet pesan user
            val states = cancelAndConsumeRemainingEvents()
            val lastItems = states.filterIsInstance<app.cash.turbine.Event.Item<ChatUiState>>()

            // minimal ada state yang punya pesan user
            val hasUserMessage = lastItems.any { event ->
                event.value.messages.any { it.isUser && it.text == "halo" }
            }
            assertTrue(hasUserMessage)
        }
    }

    @Test
    fun `resetChat clears all messages`() = runTest {
        every { mockAiRepo.chatStream(any()) } returns flowOf("response")

        viewModel.kirimPesan("test")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.resetChat()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.messages.isEmpty())
            assertFalse(state.sedangLoading)
            cancelAndIgnoreRemainingEvents()
        }

        verify { mockAiRepo.resetPercakapan() }
    }
}
