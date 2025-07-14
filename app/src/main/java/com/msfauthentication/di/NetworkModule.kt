package com.msfauthentication.di

import android.content.SharedPreferences
import com.msfauthentication.BuildConfig
import com.msfauthentication.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .callTimeout(150, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://dev-otp.yacorpt.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
}