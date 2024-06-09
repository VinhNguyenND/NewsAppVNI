package com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myappnews.Data.Enum.CommentFilter
import com.example.myappnews.Data.Firebase.Repository.ArticleRepo.ArticleRepository
import com.example.myappnews.Data.Model.Article.Field
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.Model.Comment.Comment
import com.example.myappnews.Data.Model.Source.Source
import com.example.myappnews.Data.Model.User.UserModel

class ArViewModel() : ViewModel() {
    private val ArRepository = ArticleRepository.getInstance();
    private val liveDataAr = MutableLiveData<ArrayList<NewsArticle>>();
    private val liveDataSearch = MutableLiveData<ArrayList<NewsArticle>>();
    private val liveDataHeart = MutableLiveData<ArrayList<NewsArticle>>();
    private val liveDataArticleField = MutableLiveData<ArrayList<NewsArticle>>();
    private var _isAuthen = MutableLiveData<Int>();
    private var _isForGotPassWord = MutableLiveData<Boolean>();
    private var _FieldLiveData = MutableLiveData<ArrayList<Field>>();
    private var _source = MutableLiveData<ArrayList<Source>>()
    private var _User = MutableLiveData<UserModel>();
    private var _imageByte = MutableLiveData<ByteArray>();


    companion object {
        @Volatile
        private var instance: ArViewModel? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ArViewModel().also {
                instance = it
            }
        }
    }


    fun getNewByTopic(field: String, source: String): LiveData<ArrayList<NewsArticle>> {
        ArRepository.getNewByTopic(field, source);
        ArRepository.ArticleLiveData.observeForever(Observer {
            liveDataAr.postValue(it);
        })
        return liveDataAr
    }

    fun getAllNews(): LiveData<ArrayList<NewsArticle>> {
        ArRepository.getAllNewsArticle()
        ArRepository.ArticleSearch.observeForever {
            liveDataSearch.postValue(it)
        }
        return liveDataSearch
    }

    fun getAllField(): LiveData<ArrayList<Field>> {
        ArRepository.getAllField()
        ArRepository.FieldLiveData.observeForever {
            _FieldLiveData.postValue(it);
        }
        return _FieldLiveData
    }

    fun SignIn(Activity: Activity, Email: String, PassWord: String) {
        ArRepository.Login(Activity, Email, PassWord)
        ArRepository.IsAuthen.observeForever {
            _isAuthen.postValue(it)
        }
    }

    fun forgotPassWord(Email: String) {
        ArRepository.forGotPassWord(Email)
        ArRepository.forGotPassWord.observeForever {
            _isForGotPassWord.postValue(it)
        }
    }

    fun observerForGot(): LiveData<Boolean> {
        return _isForGotPassWord;
    }

    fun OberverSignIn(): LiveData<Int> {
        return _isAuthen;
    }

    fun resetLogin(two: Int) {
        ArRepository.resetLogin(two)
        ArRepository.IsAuthen.observeForever {
            _isAuthen.postValue(it)
        }
    }

    fun getAllSource(): LiveData<ArrayList<Source>> {
        ArRepository.getAllSource()
        ArRepository.SourceAll.observeForever {
            _source.postValue(it)
        }
        return _source;
    }

    fun setImage(imageUri: ByteArray, id: String, Name: String) {
        ArRepository.setImage(imageUri, id, Name)
    }

    fun getUser(id: String): LiveData<UserModel> {
        ArRepository.getUser(id)
        ArRepository.IsUser.observeForever {
            _User.postValue(it)
        }
        return _User;
    }

    fun doLike(idDoc: String, newsArticle: NewsArticle, id: String, isLike: Boolean) {
        ArRepository.doLike(idDoc, newsArticle, id, isLike)
    }

    fun getHeart(id: String): LiveData<ArrayList<NewsArticle>> {
        ArRepository.getHeart(id)
        ArRepository.ArticleHeart.observeForever {
            liveDataHeart.postValue(it)
        }
        return liveDataHeart;
    }

    fun getArticleField(newsArticle: NewsArticle): LiveData<ArrayList<NewsArticle>> {
        ArRepository.getArticleField(newsArticle)
        ArRepository.ArticleField.observeForever {
            liveDataArticleField.postValue(it);
        }
        return liveDataArticleField;
    }


    fun postImage(byteArray: ByteArray) {
        ArRepository.postImage(byteArray)
    }

    fun getImage(): LiveData<ByteArray> {
        ArRepository.ImageByte.observeForever {
            _imageByte.postValue(it)
        }
        return _imageByte;
    }

    fun sendMainMessage(comment: Comment) {
        ArRepository.sendMainMessage(comment)
    }

    fun sendChildMessage(parent: Comment, child: Comment) {
        ArRepository.sendChildMessage(parent, child)
    }

    fun getMainMessage(type: CommentFilter, id: String): LiveData<ArrayList<Comment>> {
        ArRepository.getMainMessage(type, id)
        return ArRepository.getMainComments
    }

    fun getChildComment(comment: Comment): LiveData<ArrayList<Comment>> {
        ArRepository.getChildComment(comment)
        return ArRepository.getChildComments
    }

    fun deleteComment(parent: Comment): LiveData<Boolean> {
        ArRepository.deleteComment(parent)
        return ArRepository.deleteCommentSuccess
    }

    fun likeComment(comment: Comment, id: String, isLike: Boolean) {
        ArRepository.likeComment(comment, id, isLike)
    }

    fun removeComemt() {
        ArRepository.removeComemt()
    }
}