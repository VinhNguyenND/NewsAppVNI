package com.example.myappnews.Data.Firebase.Repository.ArticleRepo

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myappnews.Data.Model.Article.Field
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.Model.Source.Source
import com.example.myappnews.Data.Model.User.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.Calendar
import java.util.Date

class ArticleRepository {
    private val db = Firebase.firestore
    private var auth: FirebaseAuth = Firebase.auth
    private var _ArticleLiveData = MutableLiveData<ArrayList<NewsArticle>>();
    private var _FieldLiveData = MutableLiveData<ArrayList<Field>>();
    private var _isAuthen = MutableLiveData<Int>();
    private var _User = MutableLiveData<UserModel>();
    private var _source = MutableLiveData<ArrayList<Source>>()
    private var storageRef = Firebase.storage.reference

    private var timeLast = MutableLiveData<Date>()
    val ArticleLiveData: LiveData<ArrayList<NewsArticle>>
        get() = _ArticleLiveData;

    val FieldLiveData: LiveData<ArrayList<Field>>
        get() = _FieldLiveData
    val IsAuthen: LiveData<Int>
        get() = _isAuthen
    val IsUser: LiveData<UserModel>
        get() = _User
    val SourceAll: LiveData<ArrayList<Source>>
        get() = _source

    companion object {
        @Volatile
        private var instance: ArticleRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ArticleRepository().also {
                instance = it
            }
        }
    }

//    fun getAllNewsArticle(field: String) {
//        if (field == "All") {
//            db.collection("Articles")
//                .get()
//                .addOnCompleteListener {
//                    val arrayArticle = ArrayList<NewsArticle>();
//                    for (doc in it.result) {
//                        arrayArticle.add(doc.toObject<NewsArticle>())
//                    }
//                    Log.i("du lieu lay ve tu tang data", arrayArticle.toString())
//                    _ArticleLiveData.postValue(arrayArticle);
//                }
//        } else {
//            db.collection("Articles")
//                .whereEqualTo("field", field)
//                .get()
//                .addOnCompleteListener {
//                    val arrayArticle = ArrayList<NewsArticle>();
//                    for (doc in it.result) {
//                        arrayArticle.add(doc.toObject<NewsArticle>())
//                    }
//                    Log.i("du lieu lay ve tu tang data", arrayArticle.toString())
//                    _ArticleLiveData.postValue(arrayArticle);
//                }
//
//        }
//    }

    fun getNewByTopic(field: String, source: String) {
        if (field == "All" && source == "All") {
            db.collection("Articles")
                .whereEqualTo("hide", false)
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
        if (field == "All" && source != "All") {
            db.collection("Articles")
                .whereEqualTo("sourceId", source)
                .whereEqualTo("hide", false)
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

        if (field != "All" && source == "All") {
            db.collection("Articles")
                .whereEqualTo("field", field)
                .whereEqualTo("hide", false)
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

        if (field != "All" && source != "All") {
            db.collection("Articles")
                .whereEqualTo("field", field)
                .whereEqualTo("sourceId", source)
                .whereEqualTo("hide", false)
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
    }

    fun getAllSource() {
        db.collection("sources")
            .get()
            .addOnCompleteListener {
                val list = ArrayList<Source>()
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        list.add(doc.toObject<Source>())
                    }
                    _source.postValue(list)
                } else {
                    _source.postValue(list)
                }
            }
            .addOnFailureListener {
                _source.postValue(ArrayList<Source>())
            }
    }


    fun getAllField() {
        db.collection("Field")
            .orderBy("order", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener {
                val field = ArrayList<Field>();
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        field.add(doc.toObject<Field>())
                    }
                    _FieldLiveData.postValue(field);
                } else {
                    _FieldLiveData.postValue(ArrayList<Field>());
                }
            }.addOnFailureListener {
                _FieldLiveData.postValue(ArrayList<Field>());
            }
    }

    fun Login(activity: Activity, Email: String, PassWord: String) {
        auth.signInWithEmailAndPassword(Email, PassWord)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    getUsertoLogin(activity, auth.currentUser!!.uid.toString())
                } else {
                    _isAuthen.postValue(0)
                }
            }
            .addOnFailureListener {
                _isAuthen.postValue(-1)

            }
    }

    fun SignOut(activity: Activity) {
        auth.signOut()
        val sharedPref =
            activity.getSharedPreferences("News_sharedPref", Context.MODE_PRIVATE) ?: return
        sharedPref.all
    }

    fun getUser(id: String) {
        db.collection("Users")
            .whereEqualTo("idUser", id)
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    if (!it.isEmpty) {
                        var user = UserModel()
                        for (doc in it) {
                            user = doc.toObject<UserModel>()
                        }
                        _User.postValue(user)
                    }
                }

            }
    }

    fun getUsertoLogin(activity: Activity, id: String) {
        db.collection("Users")
            .whereEqualTo("idUser", id)
            .get()
            .addOnCompleteListener {
                var user = UserModel()
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        user = doc.toObject<UserModel>()
                        saveAccount(activity, user)
                        _isAuthen.postValue(1)
                    }
                } else {
                    _isAuthen.postValue(-1)
                }
            }
    }

    fun saveAccount(activity: Activity, account: UserModel) {
        val sharedPref =
            activity.getSharedPreferences("News_sharedPref", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("Uid", account.idUser)
            putString("Email", account.Email)
            putString("passWord", account.passWord)
            putString("permission", account.permission)
            putString("Name", account.Name)
            apply()
        }
    }

    fun setImage(imageUri: ByteArray, id: String) {
        val storage = storageRef.child("images/" + id + ".jpg")
        storage.putBytes(imageUri)
            .addOnSuccessListener(OnSuccessListener {
                storage.downloadUrl.addOnSuccessListener { uri ->
                    db.collection("Users")
                        .whereEqualTo("idUser", "NFiLEacfViMRSPipEXyFECarfTh1")
                        .get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                var idDoc = "0";
                                for (doc in it.result) {
                                    idDoc = doc.id;
                                }
                                db.collection("Users")
                                    .document(idDoc)
                                    .update("Image", uri)
                            }
                        }
                }
            })
    }
}