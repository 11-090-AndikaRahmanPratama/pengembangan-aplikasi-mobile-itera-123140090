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
import org.koin.androidx.compose.koinViewModel
import com.newsfeed.myprofileapp.R
import com.newsfeed.myprofileapp.viewmodel.ProfileViewModel
import com.newsfeed.myprofileapp.data.SettingsManager
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    settingsManager: SettingsManager,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkMode by settingsManager.themeFlow.collectAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf(uiState.name) }
    var editBio by remember { mutableStateOf(uiState.bio) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isDarkMode) Color(0xFF121212) else Color(0xFFF5F5F5),
        animationSpec = tween(durationMillis = 500),
        label = "bg_color_anim"
    )

    val cardBackgroundColor by animateColorAsState(
        targetValue = if (isDarkMode) Color(0xFF1E1E1E) else Color.White,
        animationSpec = tween(durationMillis = 500),
        label = "card_bg_anim"
    )

    val textColor = if (isDarkMode) Color.White else Color.Black
    val subTextColor = if (isDarkMode) Color.LightGray else Color.Gray

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
            Text("Mode Gelap", color = textColor, modifier = Modifier.padding(end = 8.dp))
            Switch(
                checked = isDarkMode,
                onCheckedChange = { isChecked ->
                    coroutineScope.launch {
                        settingsManager.saveTheme(isChecked)
                    }
                }
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
        Spacer(modifier = Modifier.height(16.dp))
        SystemInfoCard(viewModel, cardBackgroundColor, textColor)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    textColor: Color,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = textColor) },
        textStyle = androidx.compose.ui.text.TextStyle(color = textColor),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun InfoCard(cardBgColor: Color, textColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoItem(icon = Icons.Default.Email, text = "andika.123140090@student.itera.ac.id", textColor = textColor)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            InfoItem(icon = Icons.Default.Phone, text = "+62 821-XXXX-XXXX", textColor = textColor)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            InfoItem(icon = Icons.Default.LocationOn, text = "Institut Teknologi Sumatera (ITERA)", textColor = textColor)
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 14.sp, color = textColor)
    }
}

@Composable
fun PortfolioCard(cardBgColor: Color, textColor: Color) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { isExpanded = !isExpanded }) {
                Text(if (isExpanded) "Sembunyikan Portofolio" else "Tampilkan Portofolio")
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("  Penghargaan & VDP", fontWeight = FontWeight.Bold, color = textColor)
                    Text("• Apresiasi Pemerintah: KPI Pusat, BMKG, Pemprov Sulteng, dll.", fontSize = 14.sp, color = textColor)
                    Text("• Apresiasi Diskominfo: Provinsi Jambi & Kota Pontianak, dll.", fontSize = 14.sp, color = textColor)
                    Text("• Hall of Fame & CSIRT: Univ. Teknokrat Indonesia & UPT TIK ITERA", fontSize = 14.sp, color = textColor)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("  Pengalaman & Proyek", fontWeight = FontWeight.Bold, color = textColor)
                    Text("• Bug Bounty Hunter Aktif Pada Platform Hackerone dan Bugcrowd", fontSize = 14.sp, color = textColor)
                    Text("• Penetration Tester & Inisiator Komunitas RAVEN", fontSize = 14.sp, color = textColor)
                    Text("• Project: VoltGuard (KFUPM Venture Craft)", fontSize = 14.sp, color = textColor)
                }
            }
        }
    }
}

@Composable
fun SystemInfoCard(viewModel: ProfileViewModel, cardBgColor: Color, textColor: Color) {
    val isOnline by viewModel.isOnline.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Informasi Sistem", fontWeight = FontWeight.Bold, color = textColor)
            Spacer(modifier = Modifier.height(12.dp))

            Text("📱 Model: ${viewModel.getDeviceModel()}", fontSize = 14.sp, color = textColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text("🔋 Baterai: ${viewModel.getBatteryLevel()}%", fontSize = 14.sp, color = textColor)
            Spacer(modifier = Modifier.height(4.dp))

            val statusColor = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336)
            Row {
                Text("🌐 Koneksi: ", fontSize = 14.sp, color = textColor)
                Text(
                    text = if (isOnline) "Online" else "Offline",
                    fontSize = 14.sp,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}