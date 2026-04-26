package com.newsfeed.myprofileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newsfeed.myprofileapp.ui.theme.MyProfileAppTheme
import com.newsfeed.myprofileapp.screens.MainScreen
import com.newsfeed.myprofileapp.viewmodel.NotesViewModel
import com.newsfeed.myprofileapp.data.SettingsManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val notesViewModel: NotesViewModel by viewModel()
    private val settingsManager: SettingsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyProfileAppTheme {
                MainScreen(
                    notesViewModel = notesViewModel,
                    settingsManager = settingsManager
                )
            }
        }
    }
}