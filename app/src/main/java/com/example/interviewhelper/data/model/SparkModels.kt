package com.example.interviewhelper.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean
)

@JsonClass(generateAdapter = true)
data class Message(
    val role: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class ChatResponse(
    val code: Int,
    val message: String,
    val sid: String,
    val id: String,
    val created: Long,
    val choices: List<Choice>,
    val usage: Usage? = null
)

@JsonClass(generateAdapter = true)
data class Choice(
    val delta: Delta,
    val index: Int
)

@JsonClass(generateAdapter = true)
data class Delta(
    val role: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)