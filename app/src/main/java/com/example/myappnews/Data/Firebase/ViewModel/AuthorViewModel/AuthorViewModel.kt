package com.example.myappnews.Data.Firebase.ViewModel.AuthorViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myappnews.Data.Firebase.Repository.AuthorRepo.AuthorRepository
import com.example.myappnews.Data.Model.Article.NewsArticle
import java.security.MessageDigest

class AuthorViewModel : ViewModel() {
    private val _authorRepo = AuthorRepository.getInstance()
    private val _dataPosted = MutableLiveData<ArrayList<NewsArticle>>()
    private val _awaitApproval = MutableLiveData<ArrayList<NewsArticle>>()
    private val _denied = MutableLiveData<ArrayList<NewsArticle>>()
    private val _requireEdit = MutableLiveData<ArrayList<NewsArticle>>()
    private val _isRequest = MutableLiveData<Boolean?>()
    private val _isDeleteDenied = MutableLiveData<Boolean>()
    private val _isDeleteRequest = MutableLiveData<Boolean>()
    private val _isPostAgain = MutableLiveData<Boolean>()
    private val _isResponseEd = MutableLiveData<Int>();

    companion object {
        @Volatile
        private var instance: AuthorViewModel? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AuthorViewModel().also {
                instance = it
            }
        }
    }

    fun getAllPosted(id: String): LiveData<ArrayList<NewsArticle>> {
        _authorRepo.getAllPosted(id)
        _authorRepo.Posted.observeForever {
            _dataPosted.postValue(it)
        }
        return _dataPosted
    }

    fun awaitApproval(id: String): LiveData<ArrayList<NewsArticle>> {
        _authorRepo.awaitApproval(id);
        _authorRepo.awaiting.observeForever {
            _awaitApproval.postValue(it)
        }
        return _awaitApproval
    }

    fun getAllDenied(id: String): LiveData<ArrayList<NewsArticle>> {
        _authorRepo.getAllDenied(id);
        _authorRepo.Denied.observeForever {
            _denied.postValue(it)
        }
        return _denied
    }

    fun deleteDenied(newsArticle: NewsArticle): LiveData<Boolean> {
        _authorRepo.deleteDenied(newsArticle)
        _authorRepo.IsDeleteDenied.observeForever {
            _isDeleteDenied.postValue(it)
        }
        return _isDeleteDenied
    }

    fun getAllRequireEdit(id: String): LiveData<ArrayList<NewsArticle>> {
        _authorRepo.getAllRequireEdit(id);
        _authorRepo.RequireEdit.observeForever {
            _requireEdit.postValue(it)
        }
        return _requireEdit
    }

    fun sendRequest(id: String, newsArticle: NewsArticle): LiveData<Boolean?> {
        _authorRepo.sendRequest(id, newsArticle)
        _authorRepo.IsRequest.observeForever {
            _isRequest.postValue(it)
        }
        return _isRequest
    }

    fun sendArticleEdit(newsArticle: NewsArticle, imageUri: ByteArray) {
        _authorRepo.sendArticleEdit(newsArticle, imageUri)
        _authorRepo.IsRequest.observeForever {
            _isRequest.postValue(it)
        }
    }

    fun setSendArticleEdit(boolean: Boolean?) {
       _authorRepo.setSendArticleEdit(boolean)
    }

    fun observerArticleEdit(): LiveData<Boolean?> {
        return _isRequest
    }

    fun deleteArticleRequest(id: String): LiveData<Boolean> {
        _authorRepo.deleteArticleRequest(id)
        _authorRepo.IsDeleteRequest.observeForever {
            _isDeleteRequest.postValue(it)
        }
        return _isDeleteRequest
    }

    fun postAgain(id: String, newsArticle: NewsArticle): LiveData<Boolean> {
        _authorRepo.postAgain(id, newsArticle)
        _authorRepo.IsPostAgain.observeForever {
            _isPostAgain.postValue(it)
        }
        return _isPostAgain
    }

    fun responseRqEdit(newsArticle: NewsArticle, byteArray: ByteArray): LiveData<Int> {
        _authorRepo.responseRqEdit(newsArticle, byteArray)
        _authorRepo.IsResponseEd.observeForever {
            _isResponseEd.postValue(it);
        }
        return _isResponseEd;
    }

}