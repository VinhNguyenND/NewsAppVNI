package com.example.myappnews.Data.Firebase.Repository.ArticleRepo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ArticleRepository {
    private val db = Firebase.firestore
    private var _ArticleLiveData = MutableLiveData<ArrayList<NewsArticle>>();
    val ArticleLiveData: LiveData<ArrayList<NewsArticle>>
        get() = _ArticleLiveData;

    companion object {
        @Volatile
        private var instance: ArticleRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ArticleRepository().also {
                instance = it
            }
        }
    }

    fun getAllNewsArticle() {
        db.collection("Articles")
            .get()
            .addOnCompleteListener {
                val arrayArticle = ArrayList<NewsArticle>();
                for (doc in it.result) {
                    arrayArticle.add(doc.toObject<NewsArticle>())
                }
                Log.i("du lieu lay ve tu tang data",arrayArticle.toString())
                _ArticleLiveData.postValue(arrayArticle);
            }
    }

}