package com.msfauthentication.domain.repository

import com.msfauthentication.data.api.response.ProfileResponse
import com.msfauthentication.data.api.response.VerifyOTPImageResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    fun getUserProfile(): Flow<Result<ProfileResponse>>
    fun verifyOTPImage(imageFile: File): Flow<Result<VerifyOTPImageResponse>>
}