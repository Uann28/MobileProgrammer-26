package com.example.tugasakhirpasien.network

import com.example.tugasakhirpasien.model.ApiResponse
import com.example.tugasakhirpasien.model.LoginRequest
import com.example.tugasakhirpasien.model.LoginResponse
import com.example.tugasakhirpasien.model.Pasien
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("pasien")
    suspend fun getPasien(@Header("Authorization") token: String): Response<ApiResponse<List<Pasien>>>

    // Tambahan untuk PUT (Update)
    @PUT("pasien/{id}")
    suspend fun updatePasien(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body pasien: Pasien
    ): Response<ApiResponse<Pasien>>

    // Tambahan untuk DELETE (Hapus)
    @DELETE("pasien/{id}")
    suspend fun deletePasien(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>
}