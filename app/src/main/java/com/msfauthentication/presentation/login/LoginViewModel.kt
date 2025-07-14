package com.msfauthentication.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.msfauthentication.data.api.request.LoginRequest
import com.msfauthentication.data.api.response.LoginResponse
import com.msfauthentication.domain.repository.AuthRepository
import com.msfauthentication.domain.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<Result<LoginResponse>>()
    val loginState: LiveData<Result<LoginResponse>> = _loginState

    fun login(username: String, password: String, imei: String, phoneModel: String) {
        viewModelScope.launch {
            val loginRequest = LoginRequest(username, password, imei, phoneModel)
            authRepository.login(loginRequest).collect(_loginState::setValue)
        }
    }

    fun shouldShowFingerprintPrompt(): LiveData<Result<Boolean>> {
        return authRepository.shouldShowFingerprintPrompt().asLiveData()
    }
}