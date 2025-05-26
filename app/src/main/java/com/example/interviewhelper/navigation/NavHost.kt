package com.example.interviewhelper.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.interviewhelper.common.GlobalData
import com.example.interviewhelper.ui.component.HomeScreen
import com.example.interviewhelper.ui.screens.LoginScreen
import com.example.interviewhelper.ui.screens.RegisterScreen


@Composable
fun AppNavHost(globalData: GlobalData) {
    val navController = rememberNavController()

    val token by globalData.token.collectAsState(initial = null)

    // 只在首次加载时决定起始页
    val startDestination = when {
        token.isNullOrEmpty() -> "login"
        else -> "home"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(rootNavController = navController)
        }
    }
}