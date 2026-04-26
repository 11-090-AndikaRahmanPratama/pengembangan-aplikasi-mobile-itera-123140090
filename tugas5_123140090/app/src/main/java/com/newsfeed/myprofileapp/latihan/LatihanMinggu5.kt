package com.newsfeed.myprofileapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

sealed class LatihanScreen(val route: String) {
    object NoteList : LatihanScreen("note_list")
    object NoteDetail : LatihanScreen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
}

sealed class LatihanBottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : LatihanBottomNavItem("home", Icons.Default.Home, "Home")
    object Favorites : LatihanBottomNavItem("favorites", Icons.Default.Favorite, "Favorites")
    object Profile : LatihanBottomNavItem("profile", Icons.Default.Person, "Profile")
}

@Composable
fun LatihanMainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val items = listOf(LatihanBottomNavItem.Home, LatihanBottomNavItem.Favorites, LatihanBottomNavItem.Profile)
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = LatihanBottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(LatihanBottomNavItem.Home.route) {
                LatihanNoteListScreen(navController)
            }
            composable(LatihanBottomNavItem.Favorites.route) {
                LatihanSimpleScreen("Halaman Favorites")
            }
            composable(LatihanBottomNavItem.Profile.route) {
                LatihanSimpleScreen("Halaman Profile")
            }

            composable(
                route = LatihanScreen.NoteDetail.route,
                arguments = listOf(navArgument("noteId") { type = NavType.IntType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                LatihanNoteDetailScreen(noteId = noteId, navController = navController)
            }
        }
    }
}

@Composable
fun LatihanNoteListScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ini HomeScreen / NoteList")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate(LatihanScreen.NoteDetail.createRoute(noteId = 99))
        }) {
            Text("Pergi ke Detail (ID: 99)")
        }
    }
}

@Composable
fun LatihanNoteDetailScreen(noteId: Int, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ini DetailScreen")
        Text("Note ID yang diterima: $noteId", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Kembali (popBackStack)")
        }
    }
}

@Composable
fun LatihanSimpleScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title)
    }
}