package com.example.interviewhelper.data.model

// 数据模型 (为了Preview方便，也可以放在单独的data/model文件夹)
data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val role: MessageRole,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageRole {
    USER, AI
}

