package com.example.interviewhelper.data.datasource.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkClient {
    private val BASE_URL = "https://interview.jzhangluo.com"

    private val TEST_BASE_URL = "http://10.0.2.2:8000"

    private val client = OkHttpClient.Builder().build()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()!!

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(TEST_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val userService: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

}