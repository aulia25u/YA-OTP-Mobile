package com.msfauthentication.data.api.request

import com.google.gson.annotations.SerializedName

data class VerifyLoginRequest(

    @field:SerializedName("otp")
    val otp: String
)