package com.example.hisar.api

import com.example.hisar.data.Kota
import com.example.hisar.data.Message
import com.example.hisar.data.Provinsi
import com.example.hisar.data.RequestId
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface Upload {

    @Multipart
    @POST("profilePic")
    fun profileUpload(@PartMap params : HashMap<String,RequestBody>, @Part file: MultipartBody.Part ): Call<Message>

    @Multipart
    @PUT("file/edit")
    fun docEdit(@PartMap params : HashMap<String,RequestBody>,
                @Part ktp: MultipartBody.Part?,
                @Part kk:MultipartBody.Part?,
                @Part paspor:MultipartBody.Part?,
                @Part foto: MultipartBody.Part?,
                @Part akteK:MultipartBody.Part?,
                @Part akteN:MultipartBody.Part?
    ): Call<Message>

    @Multipart
    @POST("upload/promo")
    fun uploadPromo(@Part file: MultipartBody.Part): Call<Message>

    @POST("delete/promo")
    fun deletePromo(@Body id: RequestId):Call<Message>
}