package com.msfauthentication.domain.repository

import com.msfauthentication.data.api.request.LoginRequest
import com.msfauthentication.data.api.request.VerifyLoginRequest
import com.msfauthentication.data.api.response.LoginResponse
import com.msfauthentication.data.api.response.StatusMessageResponse
import com.msfauthentication.data.api.response.VerifyLoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(loginRequest: LoginRequest): Flow<Result<LoginResponse>>
    fun verifyLogin(verifyLoginRequest: VerifyLoginRequest): Flow<Result<VerifyLoginResponse>>
    fun logout(): Flow<Result<StatusMessageResponse>>
    fun isFingerprintEnabled(): Flow<Boolean>
    fun setFingerprintEnabled(enabled: Boolean)
    fun shouldShowFingerprintPrompt(): Flow<Result<Boolean>>
}