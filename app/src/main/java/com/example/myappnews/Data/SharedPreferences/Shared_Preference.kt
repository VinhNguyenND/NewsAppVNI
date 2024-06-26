package com.example.myappnews.Data.SharedPreferences

import android.app.Activity
import android.content.Context
import com.example.myappnews.Ui.Fragment.Article.comment.generateRandomAlphabet

class Shared_Preference(activity: Activity) {

    val sharedPreferences = activity.getSharedPreferences("News_sharedPref", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    fun save(filePath:String){
        editor.putString("audioPath", filePath)
        editor.apply()
    }

    fun getFilePath():String?{
        return sharedPreferences.getString("audioPath", null)
    }

    fun getUid(): String? {
        return sharedPreferences.getString("Uid", null)
    }

    fun getUidComment():String{
       var id= sharedPreferences.getString("Uid", null);
        if(id==null){
            id=generateRandomAlphabet(10);
        }
        return  id
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

    fun getName(): String? {
        return sharedPreferences.getString("Name", null)
    }


    fun isLogin(): Boolean {
        return getUid() != null
    }

    fun logout() {
        editor.remove("Uid")
        editor.remove("Email")
        editor.remove("passWord")
        editor.remove("permission")
        editor.remove("Name")
        editor.apply()
    }
}