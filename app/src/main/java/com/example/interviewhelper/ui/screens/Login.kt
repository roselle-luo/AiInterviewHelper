package com.example.interviewhelper.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.interviewhelper.R
import com.example.interviewhelper.ui.component.TopBar
import com.example.interviewhelper.ui.theme.Blossom40
import com.example.interviewhelper.ui.theme.PurpleRed
import com.example.interviewhelper.ui.theme.Rose40
import com.example.interviewhelper.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = LoginViewModel(),
    navController: NavController = rememberNavController()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        topBar = {
            TopBar("登录", navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize(),
        )
        {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "这是应用的logo展示图标"
            )
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("邮箱") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Rose40,
                    focusedLabelColor = Rose40,
                    cursorColor = Rose40
                ),
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = viewModel.verifyCode,
                onValueChange = { viewModel.onVerifyChanged(it) },
                label = { Text("验证码") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp),
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Rose40,
                    focusedLabelColor = Rose40,
                    cursorColor = Rose40
                ),
                trailingIcon = {
                    TextButton(onClick = { viewModel.onLoginClicked(context) }) {
                        Text("发送", color = Rose40)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { navController.navigate("register") }
            ) {
                Text("没有账号？去注册go ➢➢➢", color = PurpleRed)
            }
            Button(
                onClick = { viewModel.onLoginClicked(context) },
                colors = ButtonDefaults.buttonColors(containerColor = Blossom40),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("登录", letterSpacing = 12.sp, modifier = Modifier.padding(vertical = 6.dp))
            }
        }
    }
}