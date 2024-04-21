package com.ifs21039.lostfound.presentation.lostfound

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ifs21039.lostfound.data.remote.MyResult
import com.ifs21039.lostfound.data.remote.response.DataAddLostFoundResponse
import com.ifs21039.lostfound.data.remote.response.DelcomLostFoundDetailsResponse
import com.ifs21039.lostfound.data.remote.response.DelcomRegisterResponse
import com.ifs21039.lostfound.data.repository.LostFoundRepository
import com.ifs21039.lostfound.presentation.ViewModelFactory

class LostFoundViewModel(
    private val lostfoundRepository: LostFoundRepository
) : ViewModel() {
    fun getLostfound(lostfoundId: Int): LiveData<MyResult<DelcomLostFoundDetailsResponse>>{
        return lostfoundRepository.getLostfound(lostfoundId).asLiveData()
    }
    fun postLostfound(
        title: String,
        description: String,
    ): LiveData<MyResult<DataAddLostFoundResponse>>{
        return lostfoundRepository.postLostfound(
            title,
            description
        ).asLiveData()
    }
    fun putLostfound(
        lostfoundId: Int,
        title: String,
        description: String,
        isFinished: Boolean,
    ): LiveData<MyResult<DelcomRegisterResponse>> {
        return lostfoundRepository.putLostfound(
            lostfoundId,
            title,
            description,
            isFinished,
        ).asLiveData()
    }
    fun deleteLostfound(lostfoundId: Int): LiveData<MyResult<DelcomRegisterResponse>> {
        return lostfoundRepository.deleteLostfound(lostfoundId).asLiveData()
    }
//    companion object {
//        @Volatile
//        private var INSTANCE: LostFoundViewModel? = null
//        fun getInstance(
//            lostfoundRepository: LostFoundRepository
//        ): LostFoundViewModel {
//            synchronized(ViewModelFactory::class.java) {
//                INSTANCE = LostFoundViewModel(
//                    lostfoundRepository
//                )
//            }
//            return INSTANCE as LostFoundViewModel
//        }
//    }
}