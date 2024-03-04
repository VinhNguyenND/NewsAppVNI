package com.example.myappnews.Data.Api.TextToSpeech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
class ttsViewModel:ViewModel() {
    val repository=Repository()

}