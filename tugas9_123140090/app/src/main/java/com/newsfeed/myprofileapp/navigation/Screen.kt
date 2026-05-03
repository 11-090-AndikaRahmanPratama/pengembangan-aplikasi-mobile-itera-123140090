package com.newsfeed.myprofileapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object NoteList : Screen("note_list")
    object AddNote : Screen("add_note")

    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }

    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Notes : BottomNavItem(Screen.NoteList.route, Icons.AutoMirrored.Filled.List, "Notes")
    object Chat : BottomNavItem("chat", Icons.Default.MailOutline, "AI Chat")
    object Favorites : BottomNavItem("favorites", Icons.Default.Favorite, "Favorites")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}