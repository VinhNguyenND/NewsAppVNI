package com.example.myappnews.Data.Api.Dictionary

import com.example.myappnews.Data.Api.Object.DictionaryServiceBuilder
import com.example.myappnews.Data.Api.Object.TranslateServiceBuilder

class DicRepository {
    private val _DicService= DictionaryServiceBuilder.DicService
    private val _TranslateService=TranslateServiceBuilder.translate

//    private val _WordLiveData= MutableLiveData<List<Dictionary>>()
//    private val _TranslateApi=MutableLiveData<Translate>()

//    val words: LiveData<List<Dictionary>>
//    get() = _WordLiveData
//
//    val translateWord:LiveData<Translate>
//    get() = _TranslateApi

    companion object {
        @Volatile
        private var instance: DicRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: DicRepository().also {
                instance = it
            }
        }
    }

    suspend fun getDicWord(word:String)=_DicService.listWord(word)

    suspend fun getTranslate(from:String,to:String,words:String)=_TranslateService.meanWord(from,to,words)
}