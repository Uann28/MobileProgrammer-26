package com.example.tugasakhirpasien.model

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val token: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val email: String
)