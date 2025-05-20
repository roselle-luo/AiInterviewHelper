package com.example.interviewhelper.ui.screens

import android.provider.CalendarContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import com.example.interviewhelper.ui.component.TopBar
import com.example.interviewhelper.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginScreen(viewModel: LoginViewModel = LoginViewModel(), navController: NavController = rememberNavController()) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBar("登录")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(25.dp)
                .padding(top = 50.dp)
                .fillMaxSize(),
        )
        {
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("邮箱") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = viewModel.verifyCode,
                onValueChange = { viewModel.onVerifyChanged(it) },
                label = { Text("验证码") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    TextButton(onClick = { viewModel.onLoginClicked(context) }) {
                        Text("发送")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {navController.navigate("register")}
            ) {
                Text("没有账号？去注册go ➢➢➢", color = Color.Blue)
            }
            Button(
                onClick = { viewModel.onLoginClicked(context) },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            ) {
                Text("登录")
            }
        }
    }
}