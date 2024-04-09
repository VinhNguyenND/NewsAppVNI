package com.example.myappnews.Data.SharedPreferences

import android.app.Activity
import android.content.Context

class Shared_Preference(activity: Activity) {

    val sharedPreferences = activity.getSharedPreferences("News_sharedPref", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    fun getUid(): String? {
        return sharedPreferences.getString("Uid", null)
    }

    fun getEmail(): String? {
        return sharedPreferences.getString("Email", null)
    }

    fun getPassword(): String? {
        return sharedPreferences.getString("passWord", null)
    }

    fun getPermission(): String? {
        return sharedPreferences.getString("permission", null)
    }

    fun isLogin():Boolean{
        return getUid()!=null
    }

    fun logout() {
        editor.remove("Uid")
        editor.remove("Email")
        editor.remove("passWord")
        editor.remove("permission")
        editor.apply()
    }
}