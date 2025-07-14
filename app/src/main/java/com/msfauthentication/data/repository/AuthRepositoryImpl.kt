package com.msfauthentication.data.repository

import com.msfauthentication.data.api.ApiService
import com.msfauthentication.data.api.request.LoginRequest
import com.msfauthentication.data.api.request.VerifyLoginRequest
import com.msfauthentication.data.sessionmanager.SessionManager
import com.msfauthentication.domain.repository.AuthRepository
import com.msfauthentication.domain.repository.Result
import com.msfauthentication.util.getErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override fun login(loginRequest: LoginRequest) = flow {
        emit(Result.Loading)
        try {
            val response = apiService.login(loginRequest)
            sessionManager.saveTempToken(response.tempToken)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(e.getErrorMessage(), code = e.code()))
        } catch (e: IOException) {
            emit(Result.Error("Network error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Result.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    override fun verifyLogin(
        verifyLoginRequest: VerifyLoginRequest,
    ) = flow {
        emit(Result.Loading)
        try {
            val tempToken = sessionManager.getTempToken() ?: throw Exception("Temporary token not found!")
            val response = apiService.verifyLogin("Bearer $tempToken", verifyLoginRequest)
            sessionManager.saveToken(response.token)
            sessionManager.clearTempToken()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(e.getErrorMessage(), code = e.code()))
        } catch (e: IOException) {
            emit(Result.Error("Network error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Result.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    override fun logout() = flow {
        emit(Result.Loading)
        try {
            val token = sessionManager.getToken() ?: throw Exception("Temporary token not found!")
            val response = apiService.logout("Bearer $token")
            sessionManager.clearSession()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(e.getErrorMessage(), code = e.code()))
        } catch (e: IOException) {
            emit(Result.Error("Network error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Result.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    override fun isFingerprintEnabled() = flow {
        emit(sessionManager.isFingerprintEnabled())
    }

    override fun setFingerprintEnabled(enabled: Boolean) {
        sessionManager.setFingerprintEnabled(enabled)
    }

    override fun shouldShowFingerprintPrompt() = flow {
        try {
            val isFingerprintEnabled = sessionManager.isFingerprintEnabled()
            val token = sessionManager.getToken() ?: throw Exception("Token not found!")
            val response = apiService.checkToken("Bearer $token")
            val enabled = isFingerprintEnabled && response.payload?.data != null
            emit(Result.Success(enabled))
        } catch (e: Exception) {
            emit(Result.Error("Cannot check fingerprint prompt."))
        }
    }
}