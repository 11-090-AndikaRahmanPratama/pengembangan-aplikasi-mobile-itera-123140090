package com.newsfeed.myprofileapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object NewsList : Screen("news_list")
    object NewsDetail : Screen("news_detail/{newsId}") {
        fun createRoute(newsId: Int) = "news_detail/$newsId"
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object News : BottomNavItem(Screen.NewsList.route, Icons.Default.List, "News")
    object Favorites : BottomNavItem("favorites", Icons.Default.Favorite, "Favorites")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}