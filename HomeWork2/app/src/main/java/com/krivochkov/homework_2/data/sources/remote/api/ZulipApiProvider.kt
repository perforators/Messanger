package com.krivochkov.homework_2.data.sources.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object ZulipApiProvider {

    private const val API_KEY = "OXsZOSOmAeIN4s0cdl2p97AYGu32XQuk"
    private const val EMAIL = "krivochkov01@mail.ru"
    private const val BASE_URL = "https://tinkoff-android-spring-2022.zulipchat.com/api/v1/"
    private const val CONTENT_TYPE = "application/json"

    val zulipApi: ZulipApi by lazy {
        retrofit.create(ZulipApi::class.java)
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(json.asConverterFactory(CONTENT_TYPE.toMediaType()))
            .client(okHttpClient)
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(API_KEY, EMAIL))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val json = Json { ignoreUnknownKeys = true }
}