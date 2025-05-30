package com.example.interviewhelper.data.datasource.remote

import com.example.interviewhelper.common.GlobalData
import okhttp3.Interceptor
import okhttp3.Response
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

