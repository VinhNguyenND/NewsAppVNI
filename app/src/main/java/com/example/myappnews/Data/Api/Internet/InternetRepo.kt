package com.example.myappnews.Data.Api.Internet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class InternetRepo {
    private val _connectedInternet = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _connectedInternet

    companion object {
        @Volatile
        private var instance: InternetRepo? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: InternetRepo().also {
                instance = it
            }
        }
    }

    fun changeConnected(change: Boolean) {
        _connectedInternet.postValue(change);
    }
}