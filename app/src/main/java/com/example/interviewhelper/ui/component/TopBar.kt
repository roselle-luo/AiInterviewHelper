package com.example.interviewhelper.ui.component

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun TopBar(title: String) {
    val context = LocalContext.current
    return TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { Toast.makeText(context, "点击返回", Toast.LENGTH_SHORT).show()}) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "返回"
                )
            }
        }
    )
}