package com.example.myappnews.Data.Local.Article.Down

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myappnews.Data.Local.Article.History.ArticleHelper

@Database(entities = [ArticleDownEntity::class], version = 1, exportSchema = false)
abstract class ArticleDownHelper : RoomDatabase(){
    abstract fun articleDownDao(): ArticleLDownDAO

    companion object {
        private var INSTANCE: ArticleDownHelper? = null
        fun getDataBase(context: Context): ArticleDownHelper {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance;
            }
            synchronized(this) {
                val intance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDownHelper::class.java,
                    name = "article_Down_database",
                ).build()
                INSTANCE = intance
                return intance
            }
        }
    }
}