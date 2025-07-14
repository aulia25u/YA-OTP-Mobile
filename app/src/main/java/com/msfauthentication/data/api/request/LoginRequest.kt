package com.msfauthentication.data.api.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("imei")
    val imei: String,

    @field:SerializedName("phone_model")
    val phoneModel: String
)