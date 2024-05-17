package com.example.myappnews.Data.Api.Internet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InternetViewModel : ViewModel() {
    private val repository = InternetRepo.getInstance()
    private val isConnected = MutableLiveData<Boolean>()


    fun getChangeInternet(): LiveData<Boolean> {
        repository.isConnected.observeForever {
            isConnected.postValue(it)
        }
        return isConnected
    }

    fun setChangeInternet(change: Boolean) {
        repository.changeConnected(change)
    }
}