package com.krivochkov.homework_2.di.application.modules

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.krivochkov.homework_2.data.sources.remote.api.HeaderInterceptor
import com.krivochkov.homework_2.data.sources.remote.api.ZulipApi
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideZulipApi(retrofit: Retrofit): ZulipApi {
        return retrofit.create(ZulipApi::class.java)
    }

    @Provides
    fun provideRetrofit(
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideOkHttpClient(
        headerInterceptor: HeaderInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideJsonConverterFactory(json: Json): Converter.Factory {
        return json.asConverterFactory(CONTENT_TYPE.toMediaType())
    }

    @Provides
    fun provideRxJavaFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    fun provideJson(): Json {
        return Json { ignoreUnknownKeys = true }
    }

    @Provides
    fun provideHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor(API_KEY, EMAIL)
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    companion object {

        private const val API_KEY = "OXsZOSOmAeIN4s0cdl2p97AYGu32XQuk"
        private const val EMAIL = "krivochkov01@mail.ru"
        private const val BASE_URL = "https://tinkoff-android-spring-2022.zulipchat.com/api/v1/"
        private const val CONTENT_TYPE = "application/json"
    }
}