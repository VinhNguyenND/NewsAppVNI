package com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myappnews.Data.Firebase.Repository.ArticleRepo.ArticleRepository
import com.example.myappnews.Data.Model.Article.NewsArticle

class ArViewModel() : ViewModel() {
    private val ArRepository = ArticleRepository.getInstance();
    private val liveDataAr = MutableLiveData<ArrayList<NewsArticle>>();

    companion object {
        @Volatile
        private var instance: ArViewModel? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ArViewModel().also {
                instance = it
            }
        }
    }

    fun getAllArticle(): LiveData<ArrayList<NewsArticle>> {
        ArRepository.getAllNewsArticle();
        ArRepository.ArticleLiveData.observeForever(Observer {
            liveDataAr.postValue(it);
        })
        return liveDataAr
    }

}