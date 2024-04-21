package com.ifs21039.lostfound.data.remote.response

import com.google.gson.annotations.SerializedName

data class DelcomRegisterResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)
