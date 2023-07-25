package com.example.hisar.api

import com.example.hisar.data.Data
import com.example.hisar.data.Jamaah
import com.example.hisar.login.LoginRequest
import com.example.hisar.login.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiServices {
    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @POST("login")
    fun login(@Body data: LoginRequest): Call<LoginResponse>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @GET("agen")
    fun agen(@Header("x-auth-token") key: String): Call<Data>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @GET("ustad")
    fun ustad(@Header("x-auth-token") key: String): Call<Data>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @GET("jamaah")
    fun jamaah(@Header("x-auth-token") key: String): Call<Jamaah>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @GET("profile")
    fun profile(@Header("x-auth-token") key: String?): Call<Data>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @POST("agen/jamaah")
    fun jamaah(@Header("x-auth-token") key: String?,@Body id:String): Call<Jamaah>

}