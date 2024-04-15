package com.example.myappnews.Data.Firebase.Repository.AuthorRepo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest
import java.time.LocalDateTime

class AuthorRepository {
    private val db = Firebase.firestore
    private val _postedLiveData = MutableLiveData<ArrayList<NewsArticle>>()
    private val _awaitApproval = MutableLiveData<ArrayList<NewsArticle>>()
    private val _denied = MutableLiveData<ArrayList<NewsArticle>>()
    private val _requireEdit = MutableLiveData<ArrayList<NewsArticle>>()

    private val _isRequest = MutableLiveData<Boolean>()
    private val _isDeleteRequest=MutableLiveData<Boolean>()
    private val _isPostAgain=MutableLiveData<Boolean>()
    private val  _isResponseEd=MutableLiveData<Int>();

    companion object {
        @Volatile
        private var instance: AuthorRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AuthorRepository().also {
                instance = it
            }
        }
    }

    val Posted: LiveData<ArrayList<NewsArticle>>
        get() = _postedLiveData
    val awaiting: LiveData<ArrayList<NewsArticle>>
        get() = _awaitApproval
    val Denied: LiveData<ArrayList<NewsArticle>>
        get() = _denied
    val RequireEdit: LiveData<ArrayList<NewsArticle>>
        get() = _requireEdit
    val IsRequest: LiveData<Boolean>
        get() = _isRequest
    val IsDeleteRequest:LiveData<Boolean>
        get() = _isDeleteRequest
    val IsPostAgain:LiveData<Boolean>
        get() = _isPostAgain
    val IsResponseEd:LiveData<Int>
        get() = _isResponseEd

    fun getAllPosted(id: String) {
        db.collection("Articles")
            .whereEqualTo("idPoster", id)
            .whereEqualTo("isApprove", 1)
            .get()
            .addOnCompleteListener {
                val listData = ArrayList<NewsArticle>()
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        listData.add(doc.toObject<NewsArticle>())
                    }
                    _postedLiveData.postValue(listData)
                } else {
                    _postedLiveData.postValue(listData)
                }
            }.addOnFailureListener {
                _postedLiveData.postValue(ArrayList<NewsArticle>())
            }
    }

    fun awaitApproval(id: String) {
        db.collection("Articles")
            .whereEqualTo("idPoster", id)
            .whereEqualTo("isApprove", 0)
            .get()
            .addOnCompleteListener {
                val listData = ArrayList<NewsArticle>()
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        listData.add(doc.toObject<NewsArticle>())
                    }
                    _awaitApproval.postValue(listData)
                } else {
                    _awaitApproval.postValue(listData)
                }
            }.addOnFailureListener {
                _awaitApproval.postValue(ArrayList<NewsArticle>())
            }
    }

    fun getAllDenied(id: String) {
        db.collection("Articles")
            .whereEqualTo("idPoster", id)
            .whereEqualTo("isApprove", -1)
            .get()
            .addOnCompleteListener {
                val listData = ArrayList<NewsArticle>()
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        listData.add(doc.toObject<NewsArticle>())
                    }
                    _denied.postValue(listData)
                } else {
                    _denied.postValue(listData)
                }
            }.addOnFailureListener {
                _denied.postValue(ArrayList<NewsArticle>())
            }
    }

    fun getAllRequireEdit(id: String) {
        db.collection("RequestEdit")
            .whereEqualTo("idPoster", id)
            .whereEqualTo("requireEdit", 0)
            .get()
            .addOnCompleteListener {
                val listData = ArrayList<NewsArticle>()
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        listData.add(doc.toObject<NewsArticle>())

                    }
                    Log.i("du lieu lay ve tu require", listData.toString())
                    _requireEdit.postValue(listData)
                } else {
                    _requireEdit.postValue(listData)
                }
            }.addOnFailureListener {
                _requireEdit.postValue(ArrayList<NewsArticle>())
            }
    }

    fun sendRequest(id: String,newsArticle: NewsArticle) {
        db.collection("Articles")
            .document(id)
            .set(newsArticle.toMap(), SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _isRequest.postValue(true)
                } else {
                    _isRequest.postValue(false)
                }
            }.addOnFailureListener {
                _isRequest.postValue(false)
            }
    }


    fun deleteArticleRequest(id: String) {
        db.collection("Articles")
            .document(id)
            .delete()
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    _isDeleteRequest.postValue(true)
                } else {
                    _isDeleteRequest.postValue(false)
                }
            })
            .addOnFailureListener(OnFailureListener {
                _isDeleteRequest.postValue(true)
            })
    }

    fun postAgain(id: String,newsArticle: NewsArticle){
        db.collection("Articles")
            .document(id)
            .update(newsArticle.toMap())
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _isPostAgain.postValue(true);
                }else{
                    _isPostAgain.postValue(false);
                }
            }.addOnFailureListener {
                _isPostAgain.postValue(false)
            }
    }

   fun responseRqEdit(newsArticle: NewsArticle){
       db.collection("RequestEdit")
           .document(newsArticle.idArticle.toString())
           .set(newsArticle.toMap(), SetOptions.merge())
           .addOnCompleteListener {
               if(it.isSuccessful){
                  _isResponseEd.postValue(1);
               }else{
                   _isResponseEd.postValue(0)
               }
           }.addOnFailureListener {
               _isResponseEd.postValue(-1)
           }
   }

}