package com.example.interviewhelper.data.datasource.remote

import com.example.interviewhelper.common.GlobalData
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val globalData: GlobalData
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = globalData.token.value
        val request = if (!token.isNullOrEmpty()) {
            original.newBuilder()
                .addHeader("Authorization", token)
                .build()
        } else {
            original
        }

        return chain.proceed(request)
    }
}

class SparkInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", " Bearer fBkyGJiNZLGBBQzvxcxC:icdasUrhBFizLJctfDiL")
            .addHeader("Content-Type", "application/json")
            .build()
        // 打印请求 URL
        println("🔗 Request URL: ${request.url}")

        // 打印请求方法
        println("📌 Method: ${request.method}")

        // 打印请求头
        println("📤 Headers:")
        for ((name, value) in request.headers) {
            println("  $name: $value")
        }

        // 打印请求体
        val requestBody = request.body
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            val charset = requestBody.contentType()?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
            val bodyString = buffer.readString(charset)
            println("📦 Body:\n$bodyString")
        } else {
            println("📦 Body: <empty>")
        }
        return chain.proceed(request)
    }
}

