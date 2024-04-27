package com.example.myappnews.Data.Api.TextToSpeech

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
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


    suspend fun callApiTextToSpeech(activity: Activity,content: String, apiKey: String, apiHost: String){
        _textRepository.callTextToSpeech(activity,content,apiKey,apiHost);
    }



}