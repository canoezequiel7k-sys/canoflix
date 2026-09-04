package com.arigondev.canoflix.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier // 👈 Import de Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arigondev.canoflix.ui.favorites.FavoritesScreen
import com.arigondev.canoflix.ui.home.HomeScreen
import com.arigondev.canoflix.ui.profile.ProfileScreen
import com.arigondev.canoflix.ui.search.SearchScreen

// Rutas internas del menú inferior
sealed class BottomTab(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomTab("tab_home", "Inicio", Icons.Default.Home)
    object Search: BottomTab("tab_search","Buscar", Icons.Default.Search)
    object Favorites : BottomTab("tab_favorites", "Mi Lista", Icons.Default.Favorite)
    object Profile : BottomTab("tab_profile", "Perfil", Icons.Default.Person)

}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    onLogout: () -> Unit,
    onMovieClick: (Int) -> Unit
) {
    val tabs = listOf(
        BottomTab.Home,
        BottomTab.Search,
        BottomTab.Favorites,
        BottomTab.Profile
    )

    // Scaffold nos provee la estructura visual con barra inferior (bottomBar)
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = Color.White
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                tabs.forEach { tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        selected = currentRoute == tab.route,
                        onClick = {
                            if (currentRoute != tab.route) {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // NavHost interno para alternar entre Inicio, Favoritos y Perfil manteniendo el Footer visible
        NavHost(
            navController = navController,
            startDestination = BottomTab.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomTab.Home.route) {
                HomeScreen(onMovieClick = onMovieClick)
            }
            composable(BottomTab.Favorites.route) {
                FavoritesScreen()
            }
            composable(BottomTab.Profile.route) {
                ProfileScreen(onLogout = onLogout)
            }
            composable (BottomTab.Search.route){
                SearchScreen(onMovieClick = onMovieClick)
            }
        }
    }
}