package com.example.interviewhelper.data.datasource.remote

import com.example.interviewhelper.data.model.V2ChatRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface SparkApi {
    @Streaming
    @POST("v1/chat/completions")
    suspend fun getChatStream(
        @Body request: V2ChatRequest
    ): Response<ResponseBody>
}