package com.example.hisar.data

data class ReqJamaah(
    val ktp:String,
    val nama:String,
    val kelamin:String,
    val telp:String,
    val alamat:String,
    val dp:Boolean = false,
    val paket:String,
    val id:String
)
