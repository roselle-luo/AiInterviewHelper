package com.example.interviewhelper.data.repository

import com.example.interviewhelper.data.datasource.remote.NetworkClient
import com.example.interviewhelper.data.model.ApiResponse
import com.example.interviewhelper.data.model.UserInfo
import com.example.interviewhelper.data.model.UserResponse
import javax.inject.Inject

class UserRepository @Inject constructor(
) {
    suspend fun login(email: String, code: String): ApiResponse<UserResponse> {
        return NetworkClient.userService.login(email, code)
    }

    suspend fun sendCode(email: String): ApiResponse<Any?> {
        return NetworkClient.userService.sendCode(email)
    }

    suspend fun register(email: String, gender: String, age: String, school: String, subject: String): ApiResponse<UserResponse> {
        return NetworkClient.userService.register(email, gender, age, school, subject)
    }

    suspend fun getUserInfo(token: String): ApiResponse<UserInfo> {
        return NetworkClient.userService.getUserInfo(token)
    }
}