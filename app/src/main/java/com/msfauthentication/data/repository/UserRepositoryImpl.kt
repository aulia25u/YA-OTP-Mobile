package com.msfauthentication.data.repository

import android.util.Log
import com.msfauthentication.data.api.ApiService
import com.msfauthentication.data.sessionmanager.SessionManager
import com.msfauthentication.domain.repository.Result
import com.msfauthentication.domain.repository.UserRepository
import com.msfauthentication.util.getErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : UserRepository {

    override fun getUserProfile() = flow {
        emit(Result.Loading)
        try {
            val token = sessionManager.getToken() ?: throw Exception("Token not found!")
            val response = apiService.getProfile("Bearer $token")
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(e.getErrorMessage(), code = e.code()))
        } catch (e: IOException) {
            emit(Result.Error("Network error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Result.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    override fun verifyOTPImage(imageFile: File) = flow {
        emit(Result.Loading)
        try {
            val token = sessionManager.getToken() ?: throw Exception("Token not found!")
            val imageRequestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                imageRequestBody
            )
            val response = apiService.verifyOtpImage("Bearer $token", imagePart)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(e.getErrorMessage(), code = e.code()))
        } catch (e: Exception) {
            emit(Result.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)
}