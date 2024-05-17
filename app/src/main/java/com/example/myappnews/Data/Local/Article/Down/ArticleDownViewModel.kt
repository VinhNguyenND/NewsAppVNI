package com.example.myappnews.Data.Local.Article.Down

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleDownViewModel(application: Application) : AndroidViewModel(application) {
    val readAllArticle: LiveData<List<ArticleDownEntity>>
    private val _isArticleInserted = MutableLiveData<Boolean>()
    private val _isArticleDeleted=MutableLiveData<Boolean>()
    val isArticleInserted: LiveData<Boolean> get() = _isArticleInserted
    private val repository: ArticleDownRepo

    init {
        val articleDao = ArticleDownHelper.getDataBase(application).articleDownDao()
        repository = ArticleDownRepo(articleDao)
        readAllArticle = repository.readAllData
    }

    fun addArticle(articleEntity: ArticleDownEntity) {
        viewModelScope.launch {
            try {
                val isSuccess = repository.addArticle(articleEntity)
                _isArticleInserted.postValue(isSuccess)
            } catch (e: Exception) {
                _isArticleInserted.postValue(false)
            }
        }
    }

    fun  deleteById(articleEntity: ArticleDownEntity):LiveData<Boolean>{
        viewModelScope.launch {
            try {
                val  isSuccess=repository.deleteDownArticle(articleEntity.idArticle);
                _isArticleDeleted.postValue(isSuccess)
            }catch (e:Exception){
                _isArticleDeleted.postValue(false);
            }
        }
        return  _isArticleDeleted
    }

}