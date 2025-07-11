package com.example.interviewhelper.data.repository

import androidx.compose.runtime.MutableState
import com.example.interviewhelper.data.datasource.remote.NetworkClient
import com.example.interviewhelper.data.model.ChatRequest
import com.example.interviewhelper.data.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class SparkRepository @Inject constructor() {
    suspend fun fetchChatResponse(question: String, responseStream: MutableState<String>) {
        val request = ChatRequest(
            model = "generalv3",
            messages = listOf(
                Message(role = "user", content = question),
                Message(role = "system", content = "你是一个针对面试问题分析的大师，请尽量帮助用户解答疑惑")
            ),
            stream = true
        )

        try {
            val response = NetworkClient.sparkService.getChatStream(request)

            // 处理流式响应
            withContext(Dispatchers.IO) {
                response.choices.forEach { choice ->
                    responseStream.value += choice.delta.content
                }
            }
        } catch (e: Exception) {
            println("请求失败: ${e.message}")
        }
    }
}