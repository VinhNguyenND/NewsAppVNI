package com.example.myappnews.Data.Firebase.Repository.AdminRepository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class AdminRepo {
    private val db = Firebase.firestore
    private var _ArticleLiveData = MutableLiveData<ArrayList<NewsArticle>>();
    private var _isApprove = MutableLiveData<Boolean>();
    private var _isDelete = MutableLiveData<Int>();
    private val _idDocument = MutableLiveData<String>();
    private val _isRequestEdit=MutableLiveData<Int>();
    val ArticleAdminLive: LiveData<ArrayList<NewsArticle>>
        get() = _ArticleLiveData;
    val IsApprove: LiveData<Boolean>
        get() = _isApprove
    val IdDoc: LiveData<String>
        get() = _idDocument
    val IsDelete:LiveData<Int>
        get() = _isDelete
    val IsRequestEdit:LiveData<Int>
        get() = _isRequestEdit

    companion object {
        @Volatile
        private var instance: AdminRepo? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AdminRepo().also {
                instance = it
            }
        }
    }

    fun getIdDocument(idArticle: String) {
        db.collection("Articles")
            .whereEqualTo("idArticle", idArticle)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val id = document.id
                        _idDocument.postValue(id.toString())
                    }
                }
            }
    }

    fun getNewsApprove(value: Int) {
        db.collection("Articles")
            .whereEqualTo("isApprove", value)
            .get()
            .addOnCompleteListener {
                val arrayArticle = ArrayList<NewsArticle>();
                for (doc in it.result) {
                    arrayArticle.add(doc.toObject<NewsArticle>())
                }
                Log.i("du lieu lay ve tu tang data", arrayArticle.toString())
                _ArticleLiveData.postValue(arrayArticle);
            }
    }

    fun set_ArticleLiveData() {
        _ArticleLiveData.postValue(ArrayList<NewsArticle>())
    }

    fun set_DeleteAr(){
        _isDelete.postValue(0)
    }

    fun getArticleId(id: String) {
        db.collection("Articles")
            .whereEqualTo("idArticle", id)
            .get()
            .addOnCompleteListener(OnCompleteListener { })
            .addOnFailureListener(OnFailureListener { })
    }

    fun showOrHide(id: String, isFalse: Boolean) {
        db.collection("Articles")
            .document(id)
            .update("hide", isFalse)
            .addOnCompleteListener(OnCompleteListener { })
            .addOnFailureListener(OnFailureListener { })
    }

    fun upDateArticle(id: String, news: NewsArticle) {
        db.collection("Articles")
            .document(id)
            .set(news.toMap(), SetOptions.merge())
            .addOnCompleteListener(OnCompleteListener { })
            .addOnFailureListener(OnFailureListener { })
    }

    fun deleteArticle(id: String) {
        db.collection("Articles")
            .document(id)
            .delete()
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful){
                _isDelete.postValue(1)
                }else{
                _isDelete.postValue(-1)
                }
            })
            .addOnFailureListener(OnFailureListener {
                _isDelete.postValue(-1)
            })
    }

    fun approveArticle(id: String, value: Int) {
        db.collection("Articles")
            .document(id)
            .update("isApprove", value)
            .addOnCompleteListener(OnCompleteListener {
                _isApprove.postValue(true)
            })
            .addOnFailureListener(OnFailureListener {
                _isApprove.postValue(false)
            })
    }

    fun sendRequestEdit(news: Article){
        db.collection("RequestEdit")
            .document(news.idArticle.toString())
            .set(news.toMap(), SetOptions.merge())
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _isRequestEdit.postValue(0);
                }else{
                    _isRequestEdit.postValue(-1);
                }
            }
            .addOnFailureListener {
                _isRequestEdit.postValue(-1);
            }
    }

}