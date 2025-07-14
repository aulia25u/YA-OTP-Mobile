package com.msfauthentication.data.sessionmanager

import android.content.SharedPreferences
import androidx.core.content.edit
import com.msfauthentication.util.Constants.FINGERPRINT_ENABLED_KEY
import com.msfauthentication.util.Constants.TEMP_TOKEN_KEY
import com.msfauthentication.util.Constants.TOKEN_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl @Inject constructor(
    private val preferences: SharedPreferences
) : SessionManager {

    override fun isFingerprintEnabled(): Boolean {
        return preferences.getBoolean(FINGERPRINT_ENABLED_KEY, false)
    }

    override fun setFingerprintEnabled(enabled: Boolean) {
        preferences.edit {
            putBoolean(FINGERPRINT_ENABLED_KEY, enabled)
        }
    }

    override fun saveTempToken(tempToken: String?) {
        preferences.edit {
            putString(TEMP_TOKEN_KEY, tempToken)
        }
    }

    override fun getTempToken(): String? {
        return preferences.getString(TEMP_TOKEN_KEY, null)
    }

    override fun clearTempToken() {
        preferences.edit {
            remove(TEMP_TOKEN_KEY)
        }
    }

    override fun saveToken(token: String?) {
        preferences.edit {
            putString(TOKEN_KEY, token)
        }
    }

    override fun getToken(): String? {
        return preferences.getString(TOKEN_KEY, null)
    }

    override fun clearToken() {
        preferences.edit {
            remove(TOKEN_KEY)
        }
    }

    override fun isAuthenticated(): Boolean {
        return !preferences.getString(TOKEN_KEY, null).isNullOrEmpty()
    }

    override fun clearSession() {
        preferences.edit {
            clear()
        }
    }
}