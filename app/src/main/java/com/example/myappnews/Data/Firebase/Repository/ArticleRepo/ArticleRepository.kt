package com.example.myappnews.Data.Firebase.Repository.ArticleRepo

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Switch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myappnews.Data.Enum.CommentFilter
import com.example.myappnews.Data.Model.Article.Field
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.Model.Comment.Comment
import com.example.myappnews.Data.Model.Source.Source
import com.example.myappnews.Data.Model.User.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import org.apache.poi.hssf.record.PageBreakRecord.Break
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
    private var _ArticleSearch = MutableLiveData<ArrayList<NewsArticle>>();
    private var _ArticleLike = MutableLiveData<ArrayList<NewsArticle>>();
    private var _articlefield = MutableLiveData<ArrayList<NewsArticle>>();
    private var _imageByte = MutableLiveData<ByteArray>();
    private var timeLast = MutableLiveData<Date>()
    private var isSendCommentMain = MutableLiveData<Boolean>();
    private var isSendCommentChild = MutableLiveData<Boolean>();
    private var getMainComment = MutableLiveData<ArrayList<Comment>>();
    private var getChildComment = MutableLiveData<ArrayList<Comment>>();
    private var _deleteCommentSuccess = MutableLiveData<Boolean>();
    private var _forGotPassWord = MutableLiveData<Boolean>();
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
    val ArticleSearch: LiveData<ArrayList<NewsArticle>>
        get() = _ArticleSearch

    val ArticleHeart: LiveData<ArrayList<NewsArticle>>
        get() = _ArticleLike
    val ArticleField: LiveData<ArrayList<NewsArticle>>
        get() = _articlefield
    val ImageByte: LiveData<ByteArray>
        get() = _imageByte
    val IsSendComment: LiveData<Boolean>
        get() = isSendCommentMain
    val IsSendCommentChild: LiveData<Boolean>
        get() = isSendCommentChild

    val getMainComments: LiveData<ArrayList<Comment>>
        get() = getMainComment
    val getChildComments: LiveData<ArrayList<Comment>>
        get() = getChildComment
    val deleteCommentSuccess: LiveData<Boolean>
        get() = _deleteCommentSuccess
    val forGotPassWord: LiveData<Boolean>
        get() = _forGotPassWord

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
                Log.i("du lieu lay ve tu tang data", arrayArticle.toString())
                _ArticleSearch.postValue(arrayArticle);
            }
    }

    fun getNewByTopic(field: String, source: String) {
        if (field == "All" && source == "All") {
            db.collection("Articles")
                .whereEqualTo("hide", false)
                .orderBy("pubDate", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener {
                    val arrayArticle = ArrayList<NewsArticle>();
                    for (doc in it.result) {
                        arrayArticle.add(doc.toObject<NewsArticle>())
                    }
                    _ArticleLiveData.postValue(arrayArticle);
                }
        }
        if (field == "All" && source != "All") {
            db.collection("Articles")
                .whereEqualTo("sourceId", source)
                .whereEqualTo("hide", false)
                .orderBy("pubDate", Query.Direction.DESCENDING)
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
                .orderBy("pubDate", Query.Direction.DESCENDING)
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
                .orderBy("pubDate", Query.Direction.DESCENDING)
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
            .orderBy("order", Query.Direction.ASCENDING)
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
                }
            }
            .addOnFailureListener {
                _isAuthen.postValue(-1)
            }
    }

    fun forGotPassWord(Email: String) {
        auth.sendPasswordResetEmail(Email).addOnCompleteListener {
            if (it.isSuccessful) {
                _forGotPassWord.postValue(true)
            }
        }.addOnFailureListener {
            _forGotPassWord.postValue(false)
        }
    }
//    fun Login(activity: Activity, Email: String, PassWord: String) {
//
//        auth.signInWithEmailAndPassword(Email, PassWord)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    getUsertoLogin(activity, auth.currentUser!!.uid.toString())
//                } else {
//                    val exception = it.exception
//                    if (exception is FirebaseTooManyRequestsException) {
//                        Log.d("TAG_ERROR", "Too many requests. Please try again later.")
//                    }
//                    if (exception is FirebaseAuthException) {
//                        val errorCode = exception.errorCode;
//                        when (errorCode) {
//                            "ERROR_WRONG_PASSWORD" -> {
//                                Log.d("TAG_ERROR", errorCode)
//                            }
//
//                            "ERROR_USER_NOT_FOUND" -> {
//                                Log.d("TAG_ERROR", errorCode)
//                            }
//
//                            "ERROR_INVALID_CREDENTIAL" -> {
//                                Log.d("TAG_ERROR", errorCode)
//                                exception.message?.let { it1 -> Log.d("TAG_ERROR", it1) }
//                            }
//                        }
//                    }
//                }
//            }
//    }

    fun resetLogin(two: Int) {
        _isAuthen.postValue(two);
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
                    _isAuthen.postValue(0)
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

    fun setImage(imageUri: ByteArray, id: String, Name: String) {
        val storage = storageRef.child("images/" + id + ".jpg")
        storage.putBytes(imageUri)
            .addOnSuccessListener(OnSuccessListener {
                storage.downloadUrl.addOnSuccessListener { uri ->
                    db.collection("Users")
                        .whereEqualTo("idUser", id)
                        .get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                var idDoc = "0";
                                for (doc in it.result) {
                                    idDoc = doc.id;
                                }
                                val user = mapOf(
                                    "Image" to uri,
                                    "Name" to Name,
                                )
                                db.collection("Users")
                                    .document(idDoc)
                                    .update(user)
                            }
                        }
                }
            })
    }


    //    fun doLike(idUser: String, id: String) {
//        db.collection("Articles")
//            .document(id)
//            .update("like", like)
//    }
    fun doLike(iddoc: String, newsArticle: NewsArticle, id: String, isLike: Boolean) {
        var listLike = ArrayList<String>();
        if (newsArticle.like?.isNotEmpty() == true) {
            listLike = newsArticle.like!!
        }
        if (isLike) {
            listLike.add(id)
            Log.i("islike bawng true", "islike bawng true");
        } else {
            listLike.remove(id)
            Log.i("islike bawng false", "islike bawng false");
        }
        newsArticle.like = listLike
        db.collection("Articles")
            .document(iddoc)
            .update(newsArticle.toMap())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("thanh cong", "thanh cong");
                } else {
                    Log.i("that bai", "that bai");
                }
            }.addOnFailureListener {
                Log.i("that bai", "that bai");
            }
    }

    fun getHeart(id: String) {
        db.collection("Articles")
            .whereArrayContains("like", id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val arrayArticle = ArrayList<NewsArticle>();
                    for (doc in it.result) {
                        arrayArticle.add(doc.toObject<NewsArticle>())
                    }
                    _ArticleLike.postValue(arrayArticle)
                }
            }
            .addOnFailureListener {
                _ArticleLike.postValue(ArrayList<NewsArticle>());
            }
    }

    fun getArticleField(newsArticle: NewsArticle) {
        db.collection("Articles")
            .whereEqualTo("field", newsArticle.field)
//            .orderBy("pubDate", Query.Direction.DESCENDING)
            .whereEqualTo("hide", false)
            .limit(5)
            .get()
            .addOnCompleteListener {
                val arrayArticle = ArrayList<NewsArticle>();
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        arrayArticle.add(doc.toObject<NewsArticle>())
                    }
                    _articlefield.postValue(arrayArticle)
                } else {
                    _articlefield.postValue(arrayArticle)
                }
            }
            .addOnFailureListener {
                _articlefield.postValue(ArrayList<NewsArticle>())
            }
    }

    fun postImage(imageUri: ByteArray) {
        _imageByte.postValue(imageUri)
    }

    fun sendMainMessage(comment: Comment) {
        comment.idComment?.let {
            db.collection("Comments")
                .document(it)
                .set(comment.toMap(), SetOptions.merge())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isSendCommentMain.postValue(true)
                    } else {
                        isSendCommentMain.postValue(false)
                    }
                }
                .addOnFailureListener {
                    isSendCommentMain.postValue(false)
                }
        }
    }

    fun getMainMessage(type: CommentFilter, idArticle: String) {
        when (type) {
            CommentFilter.Likes -> {
                db.collection("Comments")
                    .whereEqualTo("idArticle", idArticle)
                    .get()
                    .addOnCompleteListener {
                        val list = ArrayList<Comment>()
                        if (it.isSuccessful) {
                            for (doc in it.result) {
                                list.add(doc.toObject<Comment>())
                            }
                            list.sortWith<Comment>(object : Comparator<Comment> {
                                override fun compare(
                                    time1: Comment?,
                                    time2: Comment?
                                ): Int {
                                    if (time1 != null) {
                                        if (time2 != null) {
                                            if (time1.time != null && time2.time != null)
                                                return time2.time!!.compareTo(time1.time)
                                        }
                                    }
                                    return 0
                                }
                            })
                            getMainComment.postValue(list)
                        } else {
                            getMainComment.postValue(ArrayList<Comment>())
                        }
                    }
                    .addOnFailureListener {
                        getMainComment.postValue(ArrayList<Comment>())
                    }
            }

            CommentFilter.Oldest -> {
                db.collection("Comments")
                    .whereEqualTo("idArticle", idArticle)
                    .get()
                    .addOnCompleteListener {
                        val list = ArrayList<Comment>()
                        if (it.isSuccessful) {
                            for (doc in it.result) {
                                list.add(doc.toObject<Comment>())
                            }
                            list.sortWith<Comment>(object : Comparator<Comment> {
                                override fun compare(
                                    time1: Comment?,
                                    time2: Comment?
                                ): Int {
                                    if (time1 != null) {
                                        if (time2 != null) {
                                            if (time1.time != null && time2.time != null)
                                                return time1.time!!.compareTo(time2.time)
                                        }
                                    }
                                    return 0
                                }
                            })
                            getMainComment.postValue(list)
                        } else {
                            getMainComment.postValue(list)
                        }
                    }
                    .addOnFailureListener {
                        getMainComment.postValue(ArrayList<Comment>())
                    }
            }

            CommentFilter.Latest -> {
                db.collection("Comments")
                    .whereEqualTo("idArticle", idArticle)
                    .get()
                    .addOnCompleteListener {
                        val list = ArrayList<Comment>()
                        if (it.isSuccessful) {
                            for (doc in it.result) {
                                list.add(doc.toObject<Comment>())
                            }
                            list.sortWith<Comment>(object : Comparator<Comment> {
                                override fun compare(
                                    time1: Comment?,
                                    time2: Comment?
                                ): Int {
                                    if (time1 != null) {
                                        if (time2 != null) {
                                            if (time1.time != null && time2.time != null)
                                                return time2.time!!.compareTo(time1.time)
                                        }
                                    }
                                    return 0
                                }
                            })
                            getMainComment.postValue(list)
                        }
                        if (it.isCanceled) {
                            getMainComment.postValue(ArrayList<Comment>())
                        }
                    }
                    .addOnFailureListener {
                        getMainComment.postValue(ArrayList<Comment>())
                    }
            }
        }
    }

    fun getChildComment(comment: Comment) {
        comment.idComment?.let {
            db.collection("Comments")
                .document(it)
                .collection("childComment")
                .get()
                .addOnCompleteListener {
                    val list = ArrayList<Comment>()
                    if (it.isSuccessful) {
                        for (doc in it.result) {
                            list.add(doc.toObject<Comment>())
                        }
                        getChildComment.postValue(list)
                    } else {
                        getChildComment.postValue(list)
                    }
                }
                .addOnFailureListener {
                    getChildComment.postValue(ArrayList<Comment>())
                }
        }
    }

    fun sendChildMessage(parent: Comment, child: Comment) {
        parent.idComment?.let {
            child.idComment?.let { it1 ->
                db.collection("Comments")
                    .document(it)
                    .collection("childComment")
                    .document(it1)
                    .set(child.toMap(), SetOptions.merge())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            isSendCommentChild.postValue(true)
                        } else {
                            isSendCommentChild.postValue(false)
                        }
                    }
                    .addOnFailureListener {
                        isSendCommentChild.postValue(false)
                    }
            }
        }
    }

    fun deleteComment(parent: Comment) {
        parent.idComment?.let {
            db.collection("Comments")
                .document(it)
                .delete()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _deleteCommentSuccess.postValue(true)
                    }
                }
        }
    }

    fun likeComment(comment: Comment, id: String, isLike: Boolean) {
        comment.idComment?.let {
            val listLike = comment.like
            if (isLike) {
                listLike.add(id)
            }
            comment.like = listLike
            db.collection("Comments")
                .document(it)
                .update(comment.toMap())
        }
    }

    fun removeComemt() {
        getMainComment.postValue(ArrayList<Comment>())
    }

}