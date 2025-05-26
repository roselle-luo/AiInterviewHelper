package com.example.interviewhelper.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(val token: String? = "")

@JsonClass(generateAdapter = true)
data class UserInfo(val email: String? = "", val gender: String? = "", val age: String? = "", val school: String? = "", val subject: String? = "")
