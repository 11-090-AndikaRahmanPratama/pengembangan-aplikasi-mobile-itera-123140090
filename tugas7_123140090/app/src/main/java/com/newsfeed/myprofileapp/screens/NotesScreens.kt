package com.newsfeed.myprofileapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.newsfeed.myprofileapp.db.Note
import com.newsfeed.myprofileapp.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel) {
    // Ngambil data secara real-time dari ViewModel
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // State untuk ngatur muncul/tidaknya pop-up dialog
    var showDialog by remember { mutableStateOf(false) }
    var editNote by remember { mutableStateOf<Note?>(null) }
    var titleInput by remember { mutableStateOf("") }
    var contentInput by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editNote = null
                    titleInput = ""
                    contentInput = ""
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Catatan", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // FITUR PENCARIAN
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Cari Catatan...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // DAFTAR CATATAN
            if (notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada catatan. Yuk bikin baru!", color = MaterialTheme.colorScheme.outline)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notes) { note ->
                        NoteCard(
                            note = note,
                            onClick = {
                                editNote = note
                                titleInput = note.title
                                contentInput = note.content
                                showDialog = true
                            },
                            onDelete = {
                                viewModel.deleteNote(note.id)
                            }
                        )
                    }
                }
            }
        }
    }

    // POP-UP UNTUK TAMBAH / EDIT CATATAN
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (editNote == null) "Catatan Baru" else "Edit Catatan", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Judul") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = contentInput,
                        onValueChange = { contentInput = it },
                        label = { Text("Isi Catatan") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (titleInput.isNotBlank() && contentInput.isNotBlank()) {
                        if (editNote == null) {
                            viewModel.addNote(titleInput, contentInput)
                        } else {
                            viewModel.updateNote(editNote!!.id, titleInput, contentInput)
                        }
                        showDialog = false
                    }
                }) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

// Komponen Desain untuk 1 Kotak Catatan
@Composable
fun NoteCard(note: Note, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = note.content, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                Spacer(modifier = Modifier.height(8.dp))

                // Format Tanggal
                val date = Date(note.created_at)
                val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                Text(text = format.format(date), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}