package com.msfauthentication.data.api.response

import com.google.gson.annotations.SerializedName

data class CheckTokenResponse(

	@field:SerializedName("payload")
	val payload: Payload? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Payload(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("purpose")
	val purpose: String? = null,

	@field:SerializedName("exp")
	val exp: Int? = null,

	@field:SerializedName("iat")
	val iat: Int? = null
)

data class Data(

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("username")
	val username: String? = null
)
