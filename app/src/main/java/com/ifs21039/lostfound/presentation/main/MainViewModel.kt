package com.ifs21039.lostfound.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ifs21039.lostfound.data.pref.UserModel
import com.ifs21039.lostfound.data.remote.MyResult
import com.ifs21039.lostfound.data.remote.response.DelcomItemsResponse
import com.ifs21039.lostfound.data.remote.response.DelcomRegisterResponse
import com.ifs21039.lostfound.data.repository.AuthRepository
import com.ifs21039.lostfound.data.repository.LostFoundRepository
import com.ifs21039.lostfound.presentation.ViewModelFactory
import kotlinx.coroutines.launch
class MainViewModel(
    private val authRepository: AuthRepository,
    private val todoRepository: LostFoundRepository
) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun getTodos(): LiveData<MyResult<DelcomItemsResponse>> {
        return todoRepository.getItems(null).asLiveData()
    }

    fun putTodo(
        todoId: Int,
        title: String,
        description: String,
        isFinished: Boolean,
    ): LiveData<MyResult<DelcomRegisterResponse>> {
        return todoRepository.putLostfound(
            todoId,
            title,
            description,
            isFinished,
        ).asLiveData()
    }

//    companion object {
//        @Volatile
//        private var INSTANCE: MainViewModel? = null
//        fun getInstance(
//            authRepository: AuthRepository,
//            todoRepository: LostFoundRepository
//        ): MainViewModel {
//            synchronized(ViewModelFactory::class.java) {
//                INSTANCE = MainViewModel(
//                    authRepository,
//                    todoRepository
//                )
//            }
//            return INSTANCE as MainViewModel
//        }
//    }
}