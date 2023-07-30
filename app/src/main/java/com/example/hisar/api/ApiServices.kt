package com.example.hisar.api

import com.example.hisar.data.Data
import com.example.hisar.data.DocJamaah
import com.example.hisar.data.Jamaah
import com.example.hisar.data.Message
import com.example.hisar.data.PerkabJamaah
import com.example.hisar.data.ReqAgenEdit
import com.example.hisar.data.ReqCreate
import com.example.hisar.data.RequestEditAdmin
import com.example.hisar.data.RequestId
import com.example.hisar.data.RequestPassword
import com.example.hisar.data.ResRiwayatJamaah
import com.example.hisar.data.RiwayatLogin
import com.example.hisar.login.LoginRequest
import com.example.hisar.login.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiServices {
    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @POST("login")
    fun login(@Body data: LoginRequest): Call<LoginResponse>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @GET("agen")
    fun agen(@Header("x-auth-token") key: String): Call<Data>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @HTTP(method = "DELETE", hasBody = true, path = "hapus")
    fun hapus(@Header("x-auth-token") key: String,@Body id : RequestId): Call<Message>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @GET("ustad")
    fun ustad(@Header("x-auth-token") key: String): Call<Data>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @POST("tambah")
    fun tambah(@Header("x-auth-token") key: String,@Body data: ReqCreate): Call<Message>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @PUT("agen/edit")
    fun editAgen(@Header("x-auth-token") key: String,@Body data: ReqAgenEdit): Call<Message>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @GET("jamaah")
    fun jamaah(@Header("x-auth-token") key: String): Call<Jamaah>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @GET("profile")
    fun profile(@Header("x-auth-token") key: String?): Call<Data>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @POST("agen/jamaah")
    fun jamaah(@Header("x-auth-token") key: String?,@Body id: RequestId ): Call<Jamaah>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @PUT("jamaah/dp")
    fun jamaahDp(@Header("x-auth-token") key: String?,@Body id: RequestId ): Call<Message>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @POST("jamaah/jadwal")
    fun jamaahJadwal(@Header("x-auth-token") key: String?,@Body id: RequestId ): Call<ResRiwayatJamaah>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @POST("jamaah/doc")
    fun jamaahDoc(@Header("x-auth-token") key: String?,@Body req: RequestId ): Call<DocJamaah>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @POST("jamaah/perkab")
    fun perkab(@Header("x-auth-token") key: String?,@Body id: RequestId ): Call<PerkabJamaah>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @POST("riwayat")
    fun riwayatLogin(@Header("x-auth-token") key: String?,@Body id: RequestId ): Call<RiwayatLogin>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @PUT("admin/edit")
    fun editAdmin(@Header("x-auth-token") key: String?,@Body req: RequestEditAdmin ): Call<Message>

    @Headers("x-api-key: 87d25403-c614-4988-aeed-ee0d09b55995")
    @PUT("admin/edit/pass")
    fun editPassAdmin(@Header("x-auth-token") key: String?,@Body req: RequestPassword ): Call<Message>



}