package com.example.myappnews.Data.Model.User

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserModel(
    val idUser: String? = null,
    val Email: String? = null,
    val passWord: String? = null,
    val Name: String? = null,
    val Image: String? = null,
    val permission: String? = null,
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idUser" to idUser,
            "Email" to Email,
            "passWord" to passWord,
            "Name" to Name,
            "Image" to Image,
            "permission" to permission
        )
    }
}