package com.newsfeed.myprofileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newsfeed.myprofileapp.ui.theme.MyProfileAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyProfileAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyProfileApp()
                }
            }
        }
    }
}

@Composable
fun MyProfileApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            name = "Andika Rahman Pratama",
            bio = "Cybersecurity Enthusiast | Part of RAVEN | Bug Bounty Hunter"
        )

        Spacer(modifier = Modifier.height(16.dp))

        InfoCard()

        Spacer(modifier = Modifier.height(16.dp))

        PortfolioCard()
    }
}

@Composable
fun ProfileHeader(name: String, bio: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto Profil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .padding(16.dp),
                tint = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White
            )

            Text(
                text = bio,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun InfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoItem(icon = Icons.Default.Email, text = "andika.123140090@student.itera.ac.id")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoItem(icon = Icons.Default.Phone, text = "+62 821-XXXX-XXXX")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoItem(icon = Icons.Default.LocationOn, text = "Institut Teknologi Sumatera (ITERA)")
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
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
        Text(text = text, fontSize = 14.sp)
    }
}

@Composable
fun PortfolioCard() {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
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
                    Text("  Penghargaan & VDP", fontWeight = FontWeight.Bold)
                    Text("• Apresiasi Pemerintah: KPI Pusat, BMKG, Pemprov Sulteng, dll.", fontSize = 14.sp)
                    Text("• Apresiasi Diskominfo: Provinsi Jambi & Kota Pontianak, dll.", fontSize = 14.sp)
                    Text("• Hall of Fame & CSIRT: Univ. Teknokrat Indonesia & UPT TIK ITERA", fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("  Pengalaman & Proyek", fontWeight = FontWeight.Bold)
                    Text("• Bug Bounty Hunter Aktif Pada Platform Hackerone dan Bugcrowd", fontSize = 14.sp)
                    Text("• Penetration Tester & Inisiator Komunitas RAVEN", fontSize = 14.sp)
                    Text("• Project: VoltGuard (KFUPM Venture Craft)", fontSize = 14.sp)
                }
            }
        }
    }
}