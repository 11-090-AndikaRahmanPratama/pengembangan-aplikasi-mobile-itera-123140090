package com.newsfeed.myprofileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newsfeed.myprofileapp.data.HttpClientFactory
import com.newsfeed.myprofileapp.data.NewsRepository
import com.newsfeed.myprofileapp.screens.MainScreen
import com.newsfeed.myprofileapp.ui.theme.MyProfileAppTheme
import com.newsfeed.myprofileapp.viewmodel.NewsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val client = HttpClientFactory.create()
        val repository = NewsRepository(client, applicationContext)

        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NewsViewModel(repository) as T
            }
        }

        setContent {
            MyProfileAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val newsViewModel: NewsViewModel = viewModel(factory = factory)
                    MainScreen(newsViewModel = newsViewModel)
                }
            }
        }
    }
}