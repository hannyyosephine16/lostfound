package com.ifs21039.lostfound.data.remote.response

import com.google.gson.annotations.SerializedName

data class DelcomLostFoundDetailsResponse(

	@field:SerializedName("data")
	val data: DataLostFoundDetailsResponse,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class LostFoundDetailsResponse(

	@field:SerializedName("cover")
	val cover: String?,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("author")
	val author: AuthorLostFoundDetailsResponse,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("is_completed")
	val isCompleted: Int,

	@field:SerializedName("status")
	val status: String
)

data class DataLostFoundDetailsResponse(

	@field:SerializedName("lost_found")
	val lostfound: LostFoundDetailsResponse
)

data class AuthorLostFoundDetailsResponse(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: Any
)
