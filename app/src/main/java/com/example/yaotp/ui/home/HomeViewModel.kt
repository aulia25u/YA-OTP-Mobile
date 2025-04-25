package com.example.yaotp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaotp.data.api.ApiClient
import com.example.yaotp.data.model.LoginRequest
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HomeViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.apiService.login(
                    request = LoginRequest(username, password)
                )
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    _loginResult.value = Result.success(loginResponse.message)
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "Invalid username or password"
                        403 -> "Access denied"
                        404 -> "Service not found"
                        500 -> "Server error, please try again later"
                        else -> "Login failed: ${response.message()}"
                    }
                    _loginResult.value = Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is ConnectException -> "Could not connect to server"
                    is SocketTimeoutException -> "Connection timed out"
                    is UnknownHostException -> "Could not reach server"
                    else -> e.message ?: "Login failed"
                }
                _loginResult.value = Result.failure(Exception(errorMessage))
            } finally {
                _isLoading.value = false
            }
        }
    }
}
