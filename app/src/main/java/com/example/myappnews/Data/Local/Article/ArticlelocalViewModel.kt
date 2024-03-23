package com.example.myappnews.Data.Local.Article

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticlelocalViewModel(application: Application):AndroidViewModel(application) {
     val readAllArticle:LiveData<List<ArticleEntity>>
    private val repository:ArticleRepository

    init {
        val articleDao=ArticleHelper.getDataBase(application).articleDao()
        repository= ArticleRepository(articleDao)
        readAllArticle=repository.readAllData
    }

    fun addArticle(articleEntity: ArticleEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addArticle(articleEntity);
        }
    }
    fun  deleteAllData(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllArticle()
        }
    }

}
