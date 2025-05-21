package com.example.interviewhelper.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.interviewhelper.R
import com.example.interviewhelper.ui.component.SelectFiled
import com.example.interviewhelper.ui.component.TopBar
import com.example.interviewhelper.ui.theme.Blossom40
import com.example.interviewhelper.ui.theme.PurpleRed
import com.example.interviewhelper.ui.theme.Rose40
import com.example.interviewhelper.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Preview
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = RegisterViewModel(),
    navController: NavController = rememberNavController()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        topBar = { TopBar("注册新账号", navController) }
    ) { innerPadding ->
        FlowColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.register_paint),
                contentDescription = "注册页面的插画"
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.email.value = it },
                label = { Text("邮箱") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Rose40,
                    focusedLabelColor = Rose40,
                    cursorColor = Rose40
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                SelectFiled(
                    modifier = Modifier.weight(1f),
                    viewModel.sexExpanded,
                    viewModel.sex,
                    hintText = "选择性别",
                    viewModel.sexes
                )
                Spacer(modifier = Modifier.width(16.dp))
                SelectFiled(
                    modifier = Modifier.weight(1f),
                    viewModel.ageExpanded,
                    viewModel.age,
                    hintText = "选择年龄",
                    viewModel.ages
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.school.value,
                onValueChange = { viewModel.school.value = it },
                label = { Text("学校") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Rose40,
                    focusedLabelColor = Rose40,
                    cursorColor = Rose40
                ),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.subject.value,
                onValueChange = { viewModel.subject.value = it },
                label = { Text("专业") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Rose40,
                    focusedLabelColor = Rose40,
                    cursorColor = Rose40
                ),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = viewModel.verifyCode.value,
                onValueChange = { viewModel.verifyCode.value = it },
                label = { Text("验证码") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Rose40,
                    focusedLabelColor = Rose40,
                    cursorColor = Rose40
                ),
                trailingIcon = {
                    TextButton(onClick = { viewModel.onLoginClicked(context) }) {
                        Text("发送", color = Blossom40)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { viewModel.onLoginClicked(context) },
                colors = ButtonDefaults.buttonColors(PurpleRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    "确定注册",
                    letterSpacing = 2.5.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}