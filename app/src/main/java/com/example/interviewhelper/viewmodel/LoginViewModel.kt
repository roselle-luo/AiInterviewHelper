package com.example.interviewhelper.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
        private set
    var verifyCode by mutableStateOf("")
        private set

    // 修改用户名
    fun onEmailChanged(newValue: String) {
        email = newValue
    }

    // 修改密码
    fun onVerifyChanged(newValue: String) {
        verifyCode = newValue
    }

    // 登录逻辑
    fun onLoginClicked(context: Context) {
        Toast.makeText(context, "尝试登录：用户名=$email, 密码=$verifyCode", Toast.LENGTH_SHORT).show()
        println("尝试登录：用户名=$email, 密码=$verifyCode")
        // 这里可以调用 Repository 进行登录请求
    }

}