package com.example.interviewhelper.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class V2ChatRequest(
    val model: String,
    val user: String? = null,
    val messages: List<V2Message>,
    val stream: Boolean = true,
    val tools: List<Tool>? = null
)

@JsonClass(generateAdapter = true)
data class V2Message(
    val role: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class Tool(
    val type: String,
    val web_search: WebSearch
)

@JsonClass(generateAdapter = true)
data class WebSearch(
    val enable: Boolean,
    val search_mode: String
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