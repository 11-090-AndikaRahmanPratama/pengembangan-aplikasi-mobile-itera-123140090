package com.newsfeed.myprofileapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(onNavigateToDetail: (Int) -> Unit, onOpenDrawer: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Notes") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) { Icon(Icons.Default.Menu, contentDescription = "Menu") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            Text("Daftar Catatan Anda:")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onNavigateToDetail(123) }) { // Passing noteId simulasi
                Text("Buka Catatan Rahasia (ID: 123)")
            }
        }
    }
}

@Composable
fun FavoritesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Halaman Favorites", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun AddNoteScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Halaman Tambah Catatan", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) { Text("Kembali (popBackStack)") }
    }
}

@Composable
fun NoteDetailScreen(noteId: Int, onBack: () -> Unit, onEdit: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Detail Catatan", style = MaterialTheme.typography.titleLarge)
        Text("ID yang dikirim: $noteId", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onBack) { Text("Kembali") }
            Button(onClick = onEdit) { Text("Edit Catatan") }
        }
    }
}

@Composable
fun EditNoteScreen(noteId: Int, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Edit Catatan", style = MaterialTheme.typography.titleLarge)
        Text("ID: $noteId", color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) { Text("Simpan & Kembali") }
    }
}