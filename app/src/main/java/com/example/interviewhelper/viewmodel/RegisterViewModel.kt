package com.example.interviewhelper.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    val email = mutableStateOf("")

    val verifyCode = mutableStateOf("")

    val sex = mutableStateOf("")

    val age = mutableStateOf("")

    val sexExpanded = mutableStateOf(false)

    val ageExpanded = mutableStateOf(false)

    val school = mutableStateOf("")

    val subject = mutableStateOf("")

    val sexes =  mutableListOf("男", "女")

    val ages = (1..100).map { it.toString() }.toMutableList()

    // 登录逻辑
    fun onLoginClicked(context: Context) {
        Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show()
        println("尝试登录：用户名=$email, 密码=$verifyCode")
        // 这里可以调用 Repository 进行登录请求
    }

}