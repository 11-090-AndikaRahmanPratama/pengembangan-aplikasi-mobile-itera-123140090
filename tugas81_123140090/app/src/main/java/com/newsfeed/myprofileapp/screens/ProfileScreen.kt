package com.newsfeed.myprofileapp.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.newsfeed.myprofileapp.data.SettingsManager

@Composable
fun ProfileScreen(settingsManager: SettingsManager) {

    val coroutineScope = rememberCoroutineScope()


    val isDarkMode by settingsManager.themeFlow.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Profil & Pengaturan",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))


        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mode Gelap (Dark Mode)",
                    style = MaterialTheme.typography.titleMedium
                )


                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isChecked ->

                        coroutineScope.launch {
                            settingsManager.saveTheme(isChecked)
                        }
                    }
                )
            }
        }
    }
}