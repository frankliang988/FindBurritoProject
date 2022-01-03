package com.frankliang.findburritoproject.network

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    companion object {
        const val HEADER_KEY = "Authorization"
        const val HEADER_VALUE = "Bearer ALUkBJRvw7koOL4gNFwxrpPVbSVhZrdmOY9CkJCJUNBbfLoCQestXzT6fGdL-28Q1ZY4uUHpDYhe8y1vna9pMANqBGxmCjMXFEQLfVuBHiA2inpasSheN1X_Y-7JYXYx"
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader(HEADER_KEY, HEADER_VALUE).build()
        return chain.proceed(request)
    }
}