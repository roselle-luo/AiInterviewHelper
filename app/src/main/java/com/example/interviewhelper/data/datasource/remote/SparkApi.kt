package com.example.interviewhelper.data.datasource.remote

import com.example.interviewhelper.data.model.ApiResponse
import com.example.interviewhelper.data.model.Question
import com.example.interviewhelper.data.model.V2ChatRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

interface SparkApi {
    @Streaming
    @POST("v1/chat/completions")
    suspend fun getChatStream(
        @Body request: V2ChatRequest
    ): Response<ResponseBody>

    @GET("ai/get_questions")
    suspend fun getQuestions(
        @Query("subject") subject: String,
        @Query("number") number: Int,
    ): ApiResponse<Question>

}