package com.example.interviewhelper.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(val code: Int, val message: String, val data: T? = null)


