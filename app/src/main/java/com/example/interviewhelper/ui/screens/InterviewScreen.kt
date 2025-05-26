package com.example.interviewhelper.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun Interview(navController: NavController) {
    Box (
        modifier = Modifier
    ) {
        Text(text = "模拟面试页面")
    }
}
