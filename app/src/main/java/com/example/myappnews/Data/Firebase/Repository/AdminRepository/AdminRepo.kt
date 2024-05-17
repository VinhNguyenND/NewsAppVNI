package com.example.myappnews.Data.Firebase.Repository.AdminRepository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Ui.Fragment.management.Author.Home.sha256
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.util.Date

class AdminRepo {
    private val db = Firebase.firestore
    private var _ArticleLiveData = MutableLiveData<ArrayList<NewsArticle>>();
    private var _ArticlrWaitLiveData = MutableLiveData<ArrayList<NewsArticle>>();
    private var _isApprove = MutableLiveData<Boolean>();
    private var _isDelete = MutableLiveData<Int>();
    private val _idDocument = MutableLiveData<String>();
    private val _isRequestEdit = MutableLiveData<Int>();
    private val _isUpdateSuccess = MutableLiveData<Int>();
    val ArticleAdminLive: LiveData<ArrayList<NewsArticle>>
        get() = _ArticleLiveData;
    val IsApprove: LiveData<Boolean>
        get() = _isApprove
    val IdDoc: LiveData<String>
        get() = _idDocument
    val IsDelete: LiveData<Int>
        get() = _isDelete
    val IsRequestEdit: LiveData<Int>
        get() = _isRequestEdit

    val ArticlrWaitLiveData: LiveData<ArrayList<NewsArticle>>
        get() = _ArticlrWaitLiveData;

    val IsUpdateSuccess: LiveData<Int>
        get() = _isUpdateSuccess;

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
                _ArticleLiveData.postValue(arrayArticle);
            }
    }

    fun set_ArticleLiveData() {
        _ArticleLiveData.postValue(ArrayList<NewsArticle>())
    }

    fun set_DeleteAr() {
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
                if (it.isSuccessful) {
                    _isDelete.postValue(1)
                } else {
                    _isDelete.postValue(-1)
                }
            })
            .addOnFailureListener(OnFailureListener {
                _isDelete.postValue(-1)
            })
    }

    fun approveArticle(idReview: String, id: String, value: Int) {
        db.collection("Articles")
            .document(id)
            .update("isApprove", value)
            .addOnCompleteListener(OnCompleteListener {
                _isApprove.postValue(true)
            })
            .addOnFailureListener(OnFailureListener {
                _isApprove.postValue(false)
            })
        db.collection("Articles")
            .document(id)
            .update("hide", false)
        db.collection("Articles")
            .document(id)
            .update("pubDate", FieldValue.serverTimestamp())
        db.collection("Articles")
            .document(id)
            .update("idReviewer", idReview)
    }

    fun sendRequestEdit(news: NewsArticle) {
        db.collection("RequestEdit")
            .document(news.idArticle!!)
            .set(news.toMap(), SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _isRequestEdit.postValue(0);
                } else {
                    _isRequestEdit.postValue(-1);
                }
            }
            .addOnFailureListener {
                _isRequestEdit.postValue(-1);
            }
    }

    fun getNewsAwaitEdit() {
        db.collection("RequestEdit")
            .get()
            .addOnCompleteListener {
                val arrayArticle = ArrayList<NewsArticle>();
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        arrayArticle.add(doc.toObject<NewsArticle>())
                    }
                    _ArticlrWaitLiveData.postValue(arrayArticle)
                } else {
                    _ArticlrWaitLiveData.postValue(arrayArticle)
                }
            }
            .addOnFailureListener {
                _ArticlrWaitLiveData.postValue(ArrayList<NewsArticle>())
            }
    }

    fun deleteRequired(id: String) {
        db.collection("RequestEdit")
            .document(id)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _isDelete.postValue(1)
                } else {
                    _isDelete.postValue(-1)
                }
            }
            .addOnFailureListener {
                _isDelete.postValue(-1)
            }
    }

    fun approvePush(news: NewsArticle) {
        db.collection("Articles")
            .document(news.idArticle!!)
            .set(news.toMap(), SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _isRequestEdit.postValue(0);
                } else {
                    _isRequestEdit.postValue(-1);
                }
            }
            .addOnFailureListener {
                _isRequestEdit.postValue(-1);
            }
    }

    fun publishRequired(news: NewsArticle) {
        db.collection("Articles")
            .whereEqualTo("idArticle", news.idArticle)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var id: String? = null;
                    for (doc in it.result) {
                        id = doc.id
                    }
                    if (id != null) {
                        db.collection("Articles")
                            .document(id)
                            .set(news, SetOptions.merge())
                            .addOnCompleteListener {
                                _isUpdateSuccess.postValue(1)
                            }
                            .addOnFailureListener {
                                _isUpdateSuccess.postValue(0)
                            }
                    } else {
                        _isUpdateSuccess.postValue(0)
                    }
                    db.collection("RequestEdit")
                        .document(news.idArticle.toString())
                        .delete()
                } else {
                    _isUpdateSuccess.postValue(-1)
                }
            }
            .addOnFailureListener {
                _isUpdateSuccess.postValue(-1)
            }
    }

}