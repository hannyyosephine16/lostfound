package com.ifs21039.lostfound.data.repository

import com.google.gson.Gson
import com.ifs21039.lostfound.data.remote.MyResult
import com.ifs21039.lostfound.data.remote.response.DelcomRegisterResponse
import com.ifs21039.lostfound.data.remote.retrofit.IApiService
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
class LostFoundRepository private constructor(
    private val apiService: IApiService,
) {
    fun postLostfound(
        title: String,
        description: String,
    ) = flow {
        emit(MyResult.Loading)
        try {
            //get success message
            emit(
                MyResult.Success(
                    apiService.postLostfound(title, description).data
                )
            )
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, DelcomRegisterResponse::class.java)
                        .message
                )
            )
        }
    }
    fun putLostfound(
        lostfoundId: Int,
        title: String,
        description: String,
        isFinished: Boolean,
    ) = flow {
        emit(MyResult.Loading)
        try {
            //get success message
            emit(
                MyResult.Success(
                    apiService.putLostfound(
                        lostfoundId,
                        title,
                        description,
                        if (isFinished) 1 else 0
                    )
                )
            )
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, DelcomRegisterResponse::class.java)
                        .message
                )
            )
        }
    }
    fun getItems(
        isFinished: Int?,
    ) = flow {
        emit(MyResult.Loading)
        try {
            //get success message
            emit(MyResult.Success(apiService.getItems(isFinished)))
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, DelcomRegisterResponse::class.java)
                        .message
                )
            )
        }
    }
    fun getLostfound(
        lostfoundId: Int,
    ) = flow {
        emit(MyResult.Loading)
        try {
            //get success message
            emit(MyResult.Success(apiService.getLostfound(lostfoundId)))
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, DelcomRegisterResponse::class.java)
                        .message
                )
            )
        }
    }
    fun deleteLostfound(
        lostfoundId: Int,
    ) = flow {
        emit(MyResult.Loading)
        try {
//get success message
            emit(MyResult.Success(apiService.deleteLostfound(lostfoundId)))
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, DelcomRegisterResponse::class.java)
                        .message
                )
            )
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: LostFoundRepository? = null
        fun getInstance(
            apiService: IApiService,
        ): LostFoundRepository {
            synchronized(LostFoundRepository::class.java) {
                INSTANCE = LostFoundRepository(
                    apiService
                )
            }
            return INSTANCE as LostFoundRepository
        }
    }
}