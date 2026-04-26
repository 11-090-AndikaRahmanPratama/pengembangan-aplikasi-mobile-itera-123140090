package com.newsfeed.myprofileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newsfeed.myprofileapp.ui.theme.MyProfileAppTheme
import com.newsfeed.myprofileapp.screens.MainScreen
import com.newsfeed.myprofileapp.viewmodel.NotesViewModel
import com.newsfeed.myprofileapp.data.SettingsManager
import org.koin.android.ext.android.inject // IMPORT SAKTI 1
import org.koin.androidx.viewmodel.ext.android.viewModel // IMPORT SAKTI 2

class MainActivity : ComponentActivity() {

    // 1. Ambil ViewModel & SettingsManager pake Koin
    private val notesViewModel: NotesViewModel by viewModel()
    private val settingsManager: SettingsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyProfileAppTheme {
                // 2. Oper ke MainScreen biar dalemnya gak merah juga
                MainScreen(
                    notesViewModel = notesViewModel,
                    settingsManager = settingsManager
                )
            }
        }
    }
}