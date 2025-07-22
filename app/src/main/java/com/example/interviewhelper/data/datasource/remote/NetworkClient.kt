package com.example.interviewhelper.data.datasource.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkClient {
    private const val BASE_URL = "https://interview.jzhangluo.com"

    private const val TEST_BASE_URL = "http://10.0.2.2:8000"

    private const val SPARK_BASE_URL = "https://spark-api-open.xf-yun.com"

    private val client = OkHttpClient.Builder().build()

    private val sparkClient = OkHttpClient.Builder().addInterceptor(SparkInterceptor()).build()

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

    private val sparkRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SPARK_BASE_URL)
            .client(sparkClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val userService: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val sparkService: SparkApi by lazy {
        sparkRetrofit.create(SparkApi::class.java)
    }

    val aiService: AiApi by lazy {
        retrofit.create(AiApi::class.java)
    }

}