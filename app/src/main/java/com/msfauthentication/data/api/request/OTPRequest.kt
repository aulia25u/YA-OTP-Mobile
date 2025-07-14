package com.msfauthentication.data.api.request

import com.google.gson.annotations.SerializedName

data class OTPRequest(

    @field:SerializedName("purpose")
    val purpose: String,
)