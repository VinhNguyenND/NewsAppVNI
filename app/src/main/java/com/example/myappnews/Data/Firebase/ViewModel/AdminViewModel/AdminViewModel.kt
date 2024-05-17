package com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myappnews.Data.Firebase.Repository.AdminRepository.AdminRepo
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle

class AdminViewModel : ViewModel() {
    private val ArRepository = AdminRepo.getInstance();
    private val liveDataAr = MutableLiveData<ArrayList<NewsArticle>>();
    private var _ArticlrWaitLiveData = MutableLiveData<ArrayList<NewsArticle>>();
    private val _isApprove = MutableLiveData<Boolean>();
    private val _idDocument = MutableLiveData<String>();
    private var _isDelete = MutableLiveData<Int>();
    private val _isRequestEdit = MutableLiveData<Int>();
    private val _isUpdateSuccess = MutableLiveData<Int>();

    companion object {
        @Volatile
        private var instance: AdminViewModel? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AdminViewModel().also {
                instance = it
            }
        }
    }

    fun getIdDoc(idArticle: String): LiveData<String> {
        ArRepository.getIdDocument(idArticle)
        ArRepository.IdDoc.observeForever {
            _idDocument.postValue(it)
        }
        return _idDocument
    }

    fun set_ArticleLiveData() {
        ArRepository.set_ArticleLiveData()
    }

    fun set_IsDelete() {
        ArRepository.set_DeleteAr()
    }

    fun getAllApprove(value: Int): LiveData<ArrayList<NewsArticle>> {
        ArRepository.getNewsApprove(value)
        ArRepository.ArticleAdminLive.observeForever(Observer {
            liveDataAr.postValue(it);
        })
        return liveDataAr
    }

    fun doApprove(idReview: String, id: String, value: Int): LiveData<Boolean> {
        ArRepository.approveArticle(idReview, id, value);
        ArRepository.IsApprove.observeForever {
            _isApprove.postValue(it)
        }
        return _isApprove
    }

    fun doDelete(id: String): LiveData<Int> {
        ArRepository.deleteArticle(id);
        ArRepository.IsDelete.observeForever {
            _isDelete.postValue(it)
        }
        return _isDelete
    }

    fun doHide(id: String, isFalse: Boolean) {
        ArRepository.showOrHide(id, isFalse)
    }

    fun sendRequestEdit(news: NewsArticle): LiveData<Int> {
        ArRepository.sendRequestEdit(news)
        ArRepository.IsRequestEdit.observeForever {
            _isRequestEdit.postValue(it)
        }
        return _isRequestEdit
    }

    fun getNewsAwaitEdit(): LiveData<ArrayList<NewsArticle>> {
        ArRepository.getNewsAwaitEdit()
        ArRepository.ArticlrWaitLiveData.observeForever {
            _ArticlrWaitLiveData.postValue(it)
        }
        return _ArticlrWaitLiveData;
    }

    fun deleteRequireEdit(id: String): LiveData<Int> {
        ArRepository.deleteRequired(id)
        ArRepository.IsDelete.observeForever {
            _isDelete.postValue(it)
        }
        return _isDelete
    }

    fun publishRequired(news: NewsArticle): LiveData<Int> {
        ArRepository.publishRequired(news = news)
        ArRepository.IsUpdateSuccess.observeForever {
            _isUpdateSuccess.postValue(it)
        }
        return _isUpdateSuccess
    }

    fun approvePush(news: NewsArticle): LiveData<Int> {
        ArRepository.approvePush(news)
        ArRepository.IsUpdateSuccess.observeForever {
            _isUpdateSuccess.postValue(it)
        }
        return _isUpdateSuccess
    }
}