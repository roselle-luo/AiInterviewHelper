package com.example.interviewhelper.data.datasource.remote

import com.example.interviewhelper.data.model.ApiResponse
import com.example.interviewhelper.data.model.Question
import retrofit2.http.GET
import retrofit2.http.Query

interface AiApi {
    @GET("ai/get_questions")
    suspend fun getQuestions(
        @Query("subject") subject: String,
        @Query("number") number: Int,
    ): ApiResponse<Question>
}