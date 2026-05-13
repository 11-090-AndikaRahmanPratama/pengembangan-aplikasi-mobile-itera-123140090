package com.newsfeed.myprofileapp.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import kotlinx.coroutines.launch
import com.newsfeed.myprofileapp.navigation.BottomNavItem
import com.newsfeed.myprofileapp.data.SettingsManager
import com.newsfeed.myprofileapp.viewmodel.NotesViewModel


@Composable
fun MainScreen(notesViewModel: NotesViewModel, settingsManager: SettingsManager) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val allRoutes = listOf(
        BottomNavItem.Notes.route,
        BottomNavItem.Chat.route,
        BottomNavItem.Favorites.route,
        BottomNavItem.Profile.route
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu Extra", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Pengaturan Aplikasi") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Tentang Aplikasi") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                if (currentRoute in allRoutes) {
                    NavigationBar {
                        val items = listOf(
                            BottomNavItem.Notes,
                            BottomNavItem.Chat,
                            BottomNavItem.Favorites,
                            BottomNavItem.Profile
                        )
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
            }

        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Notes.route,
                modifier = Modifier.padding(paddingValues)
            ) {

                composable(BottomNavItem.Notes.route) {
                    NotesScreen(viewModel = notesViewModel)
                }

                composable(BottomNavItem.Chat.route) {
                    ChatScreen()
                }

                composable(BottomNavItem.Favorites.route) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Halaman Favorites (Belum Dibuat)")
                    }
                }

                composable(BottomNavItem.Profile.route) {
                    ProfileScreen(settingsManager = settingsManager)
                }
            }
        }
    }
}
