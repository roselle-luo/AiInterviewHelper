package com.example.interviewhelper.data.repository

import com.example.interviewhelper.data.datasource.remote.NetworkClient.aiService
import com.example.interviewhelper.data.datasource.remote.NetworkClient.moshi
import com.example.interviewhelper.data.datasource.remote.NetworkClient.sparkService
import com.example.interviewhelper.data.model.ApiResponse
import com.example.interviewhelper.data.model.ChatResponse
import com.example.interviewhelper.data.model.Question
import com.example.interviewhelper.data.model.V2ChatRequest
import com.example.interviewhelper.data.model.V2Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import javax.inject.Inject

class SparkRepository @Inject constructor() {

    fun streamChat(question: String): Flow<String> = flow {
        val request = V2ChatRequest(
            model = "generalv3",
            messages = listOf(
                V2Message(role = "system", content = "你是一个针对面试问题分析的大师，请尽量帮助用户解答疑惑"),
                V2Message(role = "user", content = question)
            ),
            stream = true,
        )

        try {
            val response = sparkService.getChatStream(request)
            if (!response.isSuccessful) {
                println("请求失败: ${response.code()} ${response.message()}")
                return@flow
            }

            val body = response.body() ?: return@flow
            val source = body.source()
            val buffer = okio.Buffer()
            val responseAdapter = moshi.adapter(ChatResponse::class.java)

            while (!source.exhausted()) {
                source.read(buffer, 8192)
                while (true) {
                    val line = buffer.readUtf8Line() ?: break
                    if (line.startsWith("data:")) {
                        val data = line.removePrefix("data:").trim()
                        if (data == "[DONE]") return@flow

                        try {
                            val parsed = responseAdapter.fromJson(data)
                            val content = parsed?.choices?.getOrNull(0)?.delta?.content
                            if (!content.isNullOrEmpty()) {
                                emit(content) // ✅ 用 Flow 发射数据
                            }
                        } catch (e: Exception) {
                            println("解析异常：${e.message}")
                        }
                    }
                }
            }

            body.close()

        } catch (e: Exception) {
            println("请求异常：${e.message}")
        }
    }.flowOn(Dispatchers.IO) // ✅ 网络请求和IO在IO线程执行

    suspend fun getQuestions(number: Int, subject: String): ApiResponse<Question> {
        return aiService.getQuestions(subject = subject, number = number)
    }

}