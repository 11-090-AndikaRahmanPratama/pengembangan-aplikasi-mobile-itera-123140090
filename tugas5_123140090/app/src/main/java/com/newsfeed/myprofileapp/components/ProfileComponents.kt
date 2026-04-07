package com.newsfeed.myprofileapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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