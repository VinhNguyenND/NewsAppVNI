package com.example.myappnews.Data.Local.Article

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class ArticleHelper : RoomDatabase() {
    abstract fun articleDao(): ArticleDAO

    companion object {
        private var INSTANCE: ArticleHelper? = null
        fun getDataBase(context: Context): ArticleHelper {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance;
            }
            synchronized(this) {
                val intance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleHelper::class.java,
                    name = "article_database",
                ).build()
                INSTANCE=intance
                return intance
            }
        }
    }

}
