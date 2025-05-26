package com.example.interviewhelper.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.interviewhelper.ui.theme.Blossom80
import com.example.interviewhelper.ui.theme.PurpleRed
import com.example.interviewhelper.ui.theme.Rose40
import com.example.interviewhelper.ui.theme.Rose80
import com.example.interviewhelper.ui.theme.SpacePurple40
import com.example.interviewhelper.viewmodel.ProfileViewModel
import androidx.compose.runtime.getValue


@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel())  {
    val user by viewModel.userInfo.collectAsState()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Blossom80, shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "个人主页",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleRed
            )
            Spacer(modifier = Modifier.height(20.dp))

            UserInfoItem(label = "邮箱", value = user.email)
            UserInfoItem(label = "性别", value = user.gender)
            UserInfoItem(label = "年龄", value = user.age)
            UserInfoItem(label = "学校", value = user.school)
            UserInfoItem(label = "专业", value = user.subject)
        }
    }
}

@Composable
fun UserInfoItem(label: String, value: String?) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Rose40,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value.takeIf { !it.isNullOrBlank() } ?: "未填写",
            fontSize = 18.sp,
            color = SpacePurple40,
            fontWeight = FontWeight.SemiBold
        )
        Divider(
            color = Rose80,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}