package com.msfauthentication.data.api.request

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(

    @field:SerializedName("otp")
    val otp: String,

    @field:SerializedName("purpose")
    val purpose: String = "TRANSACTION_CONFIRMATION"
)