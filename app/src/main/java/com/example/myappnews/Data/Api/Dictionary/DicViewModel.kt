package com.example.myappnews.Data.Api.Dictionary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnews.Data.Model.Dictionary.DictionaryItem
import com.example.myappnews.Data.Model.Translate.Translate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DicViewModel() : ViewModel() {
    private val dicRepository = DicRepository.getInstance()
    private val _Word: MutableLiveData<List<DictionaryItem>?> = MutableLiveData()
    private val _TranslateApi = MutableLiveData<Translate>()

    val word: LiveData<List<DictionaryItem>?>
        get() = _Word
    val translateWord: LiveData<Translate>
        get() = _TranslateApi

    suspend fun listWord(theWord: String) {
         val startTime = System.nanoTime()
        viewModelScope.launch(Dispatchers.IO) {
            val result = dicRepository.getDicWord(theWord)
            if (result.isSuccessful && result.body() != null) {
                 var a=(result.body() as List<DictionaryItem>)
                 val result2= dicRepository.getTranslate("auto", "vi",(a)[0].word );
                 if(result2.isSuccessful&&result2.body()!=null){
                      val translateEndTime = System.nanoTime();
                      a[0].wordMean=(result2.body() as Translate).trans;
                     _Word.postValue(a);
                     Log.i("mất thời gian là:>>",a.toString())
                 }else{
                     _Word.postValue(a);
                 }
            } else {
                _Word.postValue(listOf());
            }
        }
    }

}