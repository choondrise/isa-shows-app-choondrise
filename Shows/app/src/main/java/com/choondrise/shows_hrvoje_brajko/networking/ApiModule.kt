package com.choondrise.shows_hrvoje_brajko.networking

import android.content.SharedPreferences
import android.widget.Toast
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.internal.addHeaderLenient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiModule {
    private const val BASE_URL = "https://tv-shows.infinum.academy/"

    lateinit var retrofit: ShowsApiService

    fun initRetrofit(preferences: SharedPreferences) {

        val okhttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .apply {
                addInterceptor(
                    Interceptor { chain ->
                        val builder = chain.request().newBuilder()
                        builder.header("token-type", "Bearer")

                        val client = preferences.getString("CLIENT", null)
                        val token = preferences.getString("ACCESS_TOKEN", null)
                        val uid = preferences.getString("UID", null)

                        if (token != null) builder.header("access-token", token!!)
                        if (client != null) builder.header("client", client)
                        if (uid != null) builder.header("uid", uid)

                        return@Interceptor chain.proceed(builder.build())
                    }
                )
            }
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                }.asConverterFactory(("application/json").toMediaType())
            )
            .client(okhttp)
            .build()
            .create(ShowsApiService::class.java)
    }
}

/**/