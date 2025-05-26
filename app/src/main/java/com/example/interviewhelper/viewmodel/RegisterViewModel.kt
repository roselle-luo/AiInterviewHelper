package com.example.interviewhelper.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.interviewhelper.common.GlobalData
import com.example.interviewhelper.data.repository.UserRepository
import com.example.interviewhelper.utils.LoadingDialogController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val globalData: GlobalData
) : ViewModel() {

    val email = mutableStateOf("")

    val sex = mutableStateOf("")

    val age = mutableStateOf("")

    val sexExpanded = mutableStateOf(false)

    val ageExpanded = mutableStateOf(false)

    val school = mutableStateOf("")

    val subject = mutableStateOf("")

    val sexes = mutableListOf("男", "女")

    val ages = (1..100).map { it.toString() }.toMutableList()

    fun onRegister(context: Context, navController: NavController) {
        LoadingDialogController.show("注册中...")
        viewModelScope.launch {
            try {
                val response = userRepository.register(
                    email.value,
                    sex.value,
                    age.value,
                    school.value,
                    subject.value
                )
                if (response.code == 200) {
                    withContext(Dispatchers.IO) {
                        globalData.saveToken(response.data?.token.toString())
                        globalData.initData()
                    }
                    Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show()
                    navController.navigate("home")
                } else {
                    Toast.makeText(context, "注册失败: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "注册失败: ${e}", Toast.LENGTH_SHORT).show()
                println(e.toString())
            } finally {
                LoadingDialogController.hide()
            }
        }
    }
}