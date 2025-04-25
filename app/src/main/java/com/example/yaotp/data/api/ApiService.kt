package com.example.yaotp.data.api

import com.example.yaotp.data.model.LoginRequest
import com.example.yaotp.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api.php")
    suspend fun login(
        @Query("api") api: String = "login",
        @Query("token") token: String = "your-secret-token-here",
        @Body request: LoginRequest
    ): Response<LoginResponse>
}
