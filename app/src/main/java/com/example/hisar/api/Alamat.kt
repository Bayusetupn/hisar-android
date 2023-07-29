package com.example.hisar.api

import com.example.hisar.data.Kota
import com.example.hisar.data.Provinsi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Alamat {

    @GET("provinsi")
    fun getProvinsi(): Call<Provinsi>
    @GET("kota?")
    fun getKota(@Query("id_provinsi") id: Int): Call<Kota>
}