package com.msfauthentication.data.sessionmanager

interface SessionManager {
    fun isFingerprintEnabled(): Boolean
    fun setFingerprintEnabled(enabled: Boolean)
    fun saveTempToken(tempToken: String?)
    fun getTempToken(): String?
    fun clearTempToken()
    fun saveToken(token: String?)
    fun getToken(): String?
    fun clearToken()
    fun isAuthenticated(): Boolean
    fun clearSession()
}