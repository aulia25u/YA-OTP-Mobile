package com.msfauthentication.data.api.response

import com.google.gson.annotations.SerializedName

data class StatusMessageResponse(

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)