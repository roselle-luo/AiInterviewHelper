package com.example.interviewhelper.data.datasource.remote

import com.example.interviewhelper.data.model.ChatRequest
import com.example.interviewhelper.data.model.ChatResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface SparkApi {
    @Streaming
    @POST("chat/completions")
    suspend fun getChatStream(
        @Body request: ChatRequest
    ): ChatResponse
}