package com.msfauthentication.data.api.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("profile")
	val profile: Profile? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("message")
	val message: String? = null,
)

data class Profile(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("mfa")
	val mfa: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("backup_email")
	val backupEmail: String? = null,

	@field:SerializedName("nationality")
	val nationality: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("birth_location")
	val birthLocation: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
