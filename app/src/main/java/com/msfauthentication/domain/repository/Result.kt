package com.msfauthentication.domain.repository

sealed class Result<out R> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val message: String? = null,
        val code: Int? = null,
    ) : Result<Nothing>()
}