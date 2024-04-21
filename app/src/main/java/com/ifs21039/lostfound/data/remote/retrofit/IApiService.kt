package com.ifs21039.lostfound.data.remote.retrofit

import com.ifs21039.lostfound.data.remote.response.DelcomAddLostFoundResponse
import com.ifs21039.lostfound.data.remote.response.DelcomItemsResponse
import com.ifs21039.lostfound.data.remote.response.DelcomLoginResponse
import com.ifs21039.lostfound.data.remote.response.DelcomLostFoundDetailsResponse
import com.ifs21039.lostfound.data.remote.response.DelcomRegisterResponse
import com.ifs21039.lostfound.data.remote.response.DelcomUserResponse

import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
interface IApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): DelcomRegisterResponse
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): DelcomLoginResponse
    @GET("users/me")
    suspend fun getMe(): DelcomUserResponse
    @FormUrlEncoded
    @POST("todos")
    suspend fun postLostfound(
        @Field("title") title: String,
        @Field("description") description: String,
    ): DelcomAddLostFoundResponse
    @FormUrlEncoded
    @PUT("todos/{id}")
    suspend fun putLostfound(
        @Path("id") todoId: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("is_finished") isFinished: Int,
    ): DelcomRegisterResponse
    @GET("todos")
    suspend fun getItems(
        @Query("is_finished") isFinished: Int?,
    ): DelcomItemsResponse
    @GET("todos/{id}")
    suspend fun getLostfound(
        @Path("id") todoId: Int,
    ): DelcomLostFoundDetailsResponse
    @DELETE("todos/{id}")
    suspend fun deleteLostfound(
        @Path("id") todoId: Int,
    ): DelcomRegisterResponse
}