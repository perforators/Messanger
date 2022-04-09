package com.krivochkov.homework_2.data.sources.remote.api

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(apiKey: String, email: String) : Interceptor {

    private val basicAccessAuthentication = Credentials.basic(email, apiKey)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val updatedRequest = request.newBuilder()
            .header(AUTHORIZATION_HEADER_NAME, basicAccessAuthentication)
            .build()
        return chain.proceed(updatedRequest)
    }

    companion object {
        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
    }
}