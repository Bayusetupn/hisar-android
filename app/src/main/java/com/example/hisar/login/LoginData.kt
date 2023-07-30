package com.example.hisar.login

data class LoginRequest(
    val username: String?,
    val password: String?
    )

data class LoginResponse(
    val status: String?,
    val to: String?,
    val role:String?
)
