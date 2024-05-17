package com.example.myappnews.Data.Model.Comment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp
import java.util.Date

@Parcelize
data class Comment(
    var idComment: String? = null,
    var idUser: String? = null,
    var idArticle: String? = null,
    var nameUser: String? = null,
    var comment: String? = null,
    var avatar:String?=null,
    var like: ArrayList<String> =ArrayList<String>(),
    var time: Date? = null,
    var numberComment: Double? = null,
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        val commentMap = HashMap<String, Any?>()
        commentMap["idComment"] = idComment
        commentMap["idUser"] = idUser
        commentMap["idArticle"] = idArticle
        commentMap["comment"] = comment
        commentMap["like"] = like
        commentMap["avatar"]=avatar
        commentMap["time"] = time
        commentMap["nameUser"] = nameUser
        commentMap["numberComment"] = numberComment
        return commentMap
    }
}