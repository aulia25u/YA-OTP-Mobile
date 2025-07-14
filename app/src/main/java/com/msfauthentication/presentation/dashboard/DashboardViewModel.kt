package com.msfauthentication.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.msfauthentication.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun isFingerprintEnabled(): LiveData<Boolean> {
        return authRepository.isFingerprintEnabled().asLiveData()
    }
}