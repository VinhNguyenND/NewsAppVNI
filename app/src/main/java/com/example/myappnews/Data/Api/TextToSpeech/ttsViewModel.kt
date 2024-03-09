package com.example.myappnews.Data.Api.TextToSpeech

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
class ttsViewModel:ViewModel() {

    private val _textRepository:Repository=Repository.getInstance();
    private val _TextToSpechApi =MutableLiveData<String>();
    val TextToSpechApi: LiveData<String>
        get() = _TextToSpechApi

    init {
        _textRepository.TextToSpechApi.observeForever(Observer {
            _TextToSpechApi.postValue(it);
        })
    }


    suspend fun callApiTextToSpeech(content: String, apiKey: String, apiHost: String){
        _textRepository.callTextToSpeech(content,apiKey,apiHost);
    }



}