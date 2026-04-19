package com.newsfeed.myprofileapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.newsfeed.myprofileapp.navigation.BottomNavItem
import com.newsfeed.myprofileapp.navigation.Screen
import com.newsfeed.myprofileapp.viewmodel.NewsViewModel

@Composable
fun MainScreen(newsViewModel: NewsViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(BottomNavItem.News.route, BottomNavItem.Favorites.route, BottomNavItem.Profile.route)) {
                NavigationBar {
                    val items = listOf(BottomNavItem.News, BottomNavItem.Favorites, BottomNavItem.Profile)
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.News.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.News.route) {
                NewsListScreen(
                    viewModel = newsViewModel,
                    onNavigateToDetail = { id -> navController.navigate(Screen.NewsDetail.createRoute(id)) }
                )
            }

            composable(BottomNavItem.Favorites.route) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Halaman Favorites (Kosong)")
                }
            }

            composable(BottomNavItem.Profile.route) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Halaman Profil")
                }
            }

            composable(
                route = Screen.NewsDetail.route,
                arguments = listOf(navArgument("newsId") { type = NavType.IntType })
            ) { backStackEntry ->
                val newsId = backStackEntry.arguments?.getInt("newsId") ?: 0
                NewsDetailScreen(
                    newsId = newsId,
                    viewModel = newsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}