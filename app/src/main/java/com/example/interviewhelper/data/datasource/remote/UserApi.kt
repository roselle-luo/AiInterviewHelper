package com.example.interviewhelper.data.datasource.remote

import com.example.interviewhelper.data.model.ApiResponse
import com.example.interviewhelper.data.model.UserInfo
import com.example.interviewhelper.data.model.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("code") code: String
    ): ApiResponse<UserResponse>

    @FormUrlEncoded
    @POST("user/send_code")
    suspend fun sendCode(
        @Field("email") email: String
    ): ApiResponse<Any?>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("age") age: String,
        @Field("school") school: String,
        @Field("subject") subject: String
    ): ApiResponse<UserResponse>

    @GET("user/get_user_info")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): ApiResponse<UserInfo>
}