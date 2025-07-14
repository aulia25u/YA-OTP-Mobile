package com.msfauthentication.presentation.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msfauthentication.data.api.request.VerifyLoginRequest
import com.msfauthentication.data.api.response.VerifyLoginResponse
import com.msfauthentication.domain.repository.AuthRepository
import com.msfauthentication.domain.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _otpState = MutableLiveData<Result<VerifyLoginResponse>>()
    val otpState: LiveData<Result<VerifyLoginResponse>> = _otpState

    fun verifyLogin(otp: String) {
        viewModelScope.launch {
            val verifyLoginRequest = VerifyLoginRequest(otp)
            authRepository.verifyLogin(verifyLoginRequest).collect(_otpState::setValue)
        }
    }
}