package com.example.hisar.data

data class ReqCreate(
    val no_ktp:String,
    val nama: String,
    val alamat: String,
    val no_telepon:String,
    val role:String?,
    val username:String,
    val password:String

    )