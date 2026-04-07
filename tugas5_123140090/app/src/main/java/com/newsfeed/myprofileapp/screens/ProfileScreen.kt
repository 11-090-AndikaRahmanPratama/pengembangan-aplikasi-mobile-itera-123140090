package com.newsfeed.myprofileapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newsfeed.myprofileapp.R
import com.newsfeed.myprofileapp.viewmodel.ProfileViewModel
import com.newsfeed.myprofileapp.components.*

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    var isEditing by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf(uiState.name) }
    var editBio by remember { mutableStateOf(uiState.bio) }

    val backgroundColor by animateColorAsState(
        targetValue = if (uiState.isDarkMode) Color(0xFF121212) else Color(0xFFF5F5F5),
        animationSpec = tween(durationMillis = 500),
        label = "bg_color_anim"
    )

    val cardBackgroundColor by animateColorAsState(
        targetValue = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color.White,
        animationSpec = tween(durationMillis = 500),
        label = "card_bg_anim"
    )

    val textColor = if (uiState.isDarkMode) Color.White else Color.Black
    val subTextColor = if (uiState.isDarkMode) Color.LightGray else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode", color = textColor, modifier = Modifier.padding(end = 8.dp))
            Switch(
                checked = uiState.isDarkMode,
                onCheckedChange = { viewModel.toggleDarkMode(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditing) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Edit Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
                    Spacer(modifier = Modifier.height(8.dp))

                    LabeledTextField(
                        label = "Name",
                        value = editName,
                        textColor = textColor,
                        onValueChange = { editName = it }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LabeledTextField(
                        label = "Bio",
                        value = editBio,
                        textColor = textColor,
                        onValueChange = { editBio = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.updateProfile(editName, editBio)
                            isEditing = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Profile")
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.foto_profil),
                        contentDescription = "Foto Profil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = uiState.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = uiState.bio, fontSize = 14.sp, color = subTextColor, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        editName = uiState.name
                        editBio = uiState.bio
                        isEditing = true
                    }) {
                        Text("Edit Profile")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        InfoCard(cardBackgroundColor, textColor)
        Spacer(modifier = Modifier.height(16.dp))
        PortfolioCard(cardBackgroundColor, textColor)
    }
}