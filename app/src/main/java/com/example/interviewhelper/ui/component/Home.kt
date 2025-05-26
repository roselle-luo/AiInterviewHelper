package com.example.interviewhelper.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.interviewhelper.ui.screens.ChatScreen
import com.example.interviewhelper.ui.screens.Interview
import com.example.interviewhelper.ui.screens.ProfileScreen
import com.example.interviewhelper.ui.screens.RelatedArticle

data class NavItem(val label: String, val icon: ImageVector, val route: String)

@SuppressLint("RestrictedApi")
@Composable
fun HomeScreen(rootNavController: NavHostController) {
    val navController = rememberNavController()
    val items = listOf(
        NavItem("模拟面试", Icons.Default.Home, "interview"),
        NavItem("ai问答", Icons.Default.Search, "chat"),
        NavItem("面经", Icons.Default.Search, "article"),
        NavItem("个人中心", Icons.Default.Person, "profile")
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { topLevelRoute ->
                    NavigationBarItem(
                        icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.label) },
                        label = { Text(topLevelRoute.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == topLevelRoute.route } == true,
                        onClick = {
                            navController.navigate(topLevelRoute.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        innerPadding ->
        NavHost(navController = navController, startDestination = "interview", Modifier.padding(innerPadding)) {
            composable("interview") {
                Interview(navController = rootNavController)
            }
            composable("chat") {
                ChatScreen(navController = rootNavController)
            }
            composable("article") {
                RelatedArticle(navController = rootNavController)
            }
            composable("profile") {
                ProfileScreen(navController = rootNavController)
            }
        }
    }
}