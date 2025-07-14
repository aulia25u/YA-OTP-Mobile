package com.msfauthentication.data.api.response

import com.google.gson.annotations.SerializedName

data class VerifyOTPImageResponse(

	@field:SerializedName("hash_fragment")
	val hashFragment: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
