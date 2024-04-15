package com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myappnews.Data.Firebase.Repository.ArticleRepo.ArticleRepository
import com.example.myappnews.Data.Model.Article.Field
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.Model.Source.Source
import com.example.myappnews.Data.Model.User.UserModel

class ArViewModel() : ViewModel() {
    private val ArRepository = ArticleRepository.getInstance();
    private val liveDataAr = MutableLiveData<ArrayList<NewsArticle>>();
    private var _isAuthen = MutableLiveData<Int>();
    private var _FieldLiveData = MutableLiveData<ArrayList<Field>>();
    private var _source = MutableLiveData<ArrayList<Source>>()
    private var _User = MutableLiveData<UserModel>();


    companion object {
        @Volatile
        private var instance: ArViewModel? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ArViewModel().also {
                instance = it
            }
        }
    }

//    fun getAllArticle(field: String): LiveData<ArrayList<NewsArticle>> {
//        ArRepository.getAllNewsArticle(field);
//        ArRepository.ArticleLiveData.observeForever(Observer {
//            liveDataAr.postValue(it);
//        })
//        return liveDataAr
//    }

    fun getNewByTopic(field: String, source: String): LiveData<ArrayList<NewsArticle>> {
        ArRepository.getNewByTopic(field, source);
        ArRepository.ArticleLiveData.observeForever(Observer {
            liveDataAr.postValue(it);
        })
        return liveDataAr
    }


    fun getAllField(): LiveData<ArrayList<Field>> {
        ArRepository.getAllField()
        ArRepository.FieldLiveData.observeForever {
            _FieldLiveData.postValue(it);
        }
        return _FieldLiveData
    }

    fun SignIn(Activity: Activity, Email: String, PassWord: String): LiveData<Int> {
        ArRepository.Login(Activity, Email, PassWord)
        ArRepository.IsAuthen.observeForever {
            _isAuthen.postValue(it)
        }
        return _isAuthen;
    }

    fun getAllSource(): LiveData<ArrayList<Source>> {
        ArRepository.getAllSource()
        ArRepository.SourceAll.observeForever {
            _source.postValue(it)
        }
        return _source;
    }

    fun setImage(imageUri: ByteArray, id: String) {
        ArRepository.setImage(imageUri, id)
    }

    fun getUser(id: String): LiveData<UserModel> {
        ArRepository.getUser(id)
        ArRepository.IsUser.observeForever {
            _User.postValue(it)
        }
        return _User;
    }


}