package com.example.yaotp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    // For demo purposes, we'll use dummy data
    private val _userData = MutableLiveData<UserData>().apply {
        value = UserData(
            username = "Demo User",
            email = "demo.user@example.com"
        )
    }
    val userData: LiveData<UserData> = _userData

    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> = _logoutResult

    fun logout() {
        // In a real app, clear user session, tokens, etc.
        _logoutResult.value = true
    }

    data class UserData(
        val username: String,
        val email: String
    )
}
