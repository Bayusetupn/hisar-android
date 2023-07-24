package com.example.hisar.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {


        private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        private val okhttp: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(logging)

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api-0ych.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttp.build())
            .build()
        val apiService: ApiServices = retrofit.create(ApiServices::class.java)
    }

