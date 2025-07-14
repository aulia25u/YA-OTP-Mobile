package com.msfauthentication.data.api.response

import com.google.gson.annotations.SerializedName

data class VerifyLoginResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
