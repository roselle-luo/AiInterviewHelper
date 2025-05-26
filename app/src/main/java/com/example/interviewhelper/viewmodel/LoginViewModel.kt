package com.example.interviewhelper.viewmodel

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.interviewhelper.common.GlobalData
import com.example.interviewhelper.data.model.ApiResponse
import com.example.interviewhelper.data.repository.UserRepository
import com.example.interviewhelper.utils.LoadingDialogController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val globalData: GlobalData
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var verifyCode by mutableStateOf("")
        private set

    val countDownTime = mutableIntStateOf(0)

    // 修改用户名
    fun onEmailChanged(newValue: String) {
        email = newValue
    }

    // 修改密码
    fun onVerifyChanged(newValue: String) {
        verifyCode = newValue
    }

    fun startVerify(context: Context) {
        if (checkEmail(email)) {
            viewModelScope.launch {
                val success = sendCode(email, context)
                if (success) startCountDown()
            }
        } else Toast.makeText(context, "邮箱格式错误", Toast.LENGTH_SHORT).show()
    }

    // 发送验证码
    suspend fun sendCode(email: String, context: Context): Boolean {
        LoadingDialogController.show("正在发送中...")
        lateinit var response: ApiResponse<Unit>
        return try {
            val response = userRepository.sendCode(email) // 这里直接调用挂起函数，等待结果
            response.code == 200
        } catch (e: Exception) {
            Log.e("发送错误", e.toString())
            Toast.makeText(context, "发送错误：$e", Toast.LENGTH_SHORT).show()
            false
        } finally {
            LoadingDialogController.hide()
        }
    }

    fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun startCountDown() {
        countDownTime.value = 120
        viewModelScope.launch {
            while (countDownTime.value > 0) {
                delay(1000)
                countDownTime.value--
            }
        }
    }

    // 登录逻辑
    fun onLoginClicked(context: Context, navController: NavController) {
        LoadingDialogController.show("正在登录中...")
        viewModelScope.launch {
            try {
                val response = userRepository.login(email, verifyCode)
                if (response.code == 200) {
                    Log.d("登录成功0", response.data?.token.toString())
                    try {
                        Log.d("登录成功0.5", "准备保存token")
                        globalData.saveToken(response.data?.token.toString())
                        Log.d("登录成功0.5.5", "这初始化")
                        globalData.initData()
                        Log.d("登录成功0.6", "初始化完成")
                    } catch (e: Exception) {
                        Log.e("登录成功-初始化异常", e.toString())
                    }

                    withContext(Dispatchers.Main) {
                        Log.d("登录成功2", response.data?.token.toString())
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
                        navController.navigate("home")
                        Log.d("登录成功3", response.data?.token.toString())
                    }
                } else Toast.makeText(context, "登录失败：${response.message}", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                Toast.makeText(context, "登录错误：$e", Toast.LENGTH_SHORT).show()
            } finally {
                LoadingDialogController.hide()
            }
        }
    }

}