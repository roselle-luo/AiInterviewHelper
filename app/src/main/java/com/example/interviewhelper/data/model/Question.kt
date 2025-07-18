package com.example.interviewhelper.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Question(val questions: List<String> = emptyList<String>(), val number: Int? = 0)
