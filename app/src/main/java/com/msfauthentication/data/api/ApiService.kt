package com.msfauthentication.data.api

import com.msfauthentication.data.api.request.LoginRequest
import com.msfauthentication.data.api.request.OTPRequest
import com.msfauthentication.data.api.request.VerifyLoginRequest
import com.msfauthentication.data.api.request.VerifyOTPRequest
import com.msfauthentication.data.api.response.CheckTokenResponse
import com.msfauthentication.data.api.response.LoginResponse
import com.msfauthentication.data.api.response.ProfileResponse
import com.msfauthentication.data.api.response.StatusMessageResponse
import com.msfauthentication.data.api.response.VerifyLoginResponse
import com.msfauthentication.data.api.response.VerifyOTPImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @POST("api.php?api=login")
    @Headers("Content-Type: application/json",)
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("api.php?api=verify-login")
    @Headers("Content-Type: application/json")
    suspend fun verifyLogin(
        @Header("Authorization") tempToken: String,
        @Body verifyLoginRequest: VerifyLoginRequest,
    ): VerifyLoginResponse

    @GET("api.php?api=profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): ProfileResponse

    @Multipart
    @POST("api.php?api=verify-otp-image")
    suspend fun verifyOtpImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part?
    ): VerifyOTPImageResponse

    @POST("api.php?api=request-otp")
    @Headers("Content-Type: application/json")
    suspend fun requestOtp(
        @Header("Authorization") token: String,
        @Body otpRequest: OTPRequest
    ): StatusMessageResponse

    @POST("api.php?api=verify-otp")
    @Headers("Content-Type: application/json")
    suspend fun verifyOtp(
        @Header("Authorization") token: String,
        @Body verifyOTPRequest: VerifyOTPRequest
    ): StatusMessageResponse

    @GET("api.php?api=check-token")
    suspend fun checkToken(
        @Header("Authorization") token: String,
    ): CheckTokenResponse

    @POST("api.php?api=logout")
    @Headers("Content-Type: application/json")
    suspend fun logout(
        @Header("Authorization") token: String,
    ): StatusMessageResponse
}