package com.msfauthentication.presentation.uploadimage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msfauthentication.data.api.response.VerifyOTPImageResponse
import com.msfauthentication.domain.repository.AuthRepository
import com.msfauthentication.domain.repository.Result
import com.msfauthentication.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadImageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uploadImageState = MutableLiveData<Result<VerifyOTPImageResponse>>()
    val uploadImageState: LiveData<Result<VerifyOTPImageResponse>> = _uploadImageState

    fun uploadImage(imageFile: File) {
        viewModelScope.launch {
            userRepository.verifyOTPImage(imageFile).collect(_uploadImageState::setValue)
        }
    }
}