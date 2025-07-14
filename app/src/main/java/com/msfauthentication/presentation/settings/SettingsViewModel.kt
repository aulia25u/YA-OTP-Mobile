package com.msfauthentication.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.msfauthentication.data.api.response.StatusMessageResponse
import com.msfauthentication.domain.repository.AuthRepository
import com.msfauthentication.domain.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _logoutState = MutableLiveData<Result<StatusMessageResponse>>()
    val logoutState: LiveData<Result<StatusMessageResponse>> = _logoutState

    fun logout() {
        viewModelScope.launch {
            authRepository.logout().collect(_logoutState::setValue)
        }
    }

    fun setFingerprintEnabled(enabled: Boolean) {
        authRepository.setFingerprintEnabled(enabled)
    }

    fun isFingerprintEnabled(): LiveData<Boolean> {
        return authRepository.isFingerprintEnabled().asLiveData()
    }
}