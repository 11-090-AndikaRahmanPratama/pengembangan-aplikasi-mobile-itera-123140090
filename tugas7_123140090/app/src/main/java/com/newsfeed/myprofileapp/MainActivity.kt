package com.newsfeed.myprofileapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newsfeed.myprofileapp.data.DatabaseProvider
import com.newsfeed.myprofileapp.data.NoteRepository
import com.newsfeed.myprofileapp.data.SettingsManager
import com.newsfeed.myprofileapp.screens.MainScreen
import com.newsfeed.myprofileapp.ui.theme.MyProfileAppTheme
import com.newsfeed.myprofileapp.viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val database = DatabaseProvider.getDatabase(this)
        val repository = NoteRepository(database)
        val settingsManager = SettingsManager(this)

        setContent {

            val systemTheme = isSystemInDarkTheme()
            val isDarkMode by settingsManager.themeFlow.collectAsState(initial = systemTheme)


            MyProfileAppTheme(darkTheme = isDarkMode) {


                val factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return NotesViewModel(repository) as T
                    }
                }

                val notesViewModel: NotesViewModel = viewModel(factory = factory)



                 MainScreen(
                    notesViewModel = notesViewModel,
                    settingsManager = settingsManager
                )

            }
        }
    }
}