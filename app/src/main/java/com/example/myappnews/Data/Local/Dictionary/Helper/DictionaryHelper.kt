package com.example.myappnews.Data.Local.Dictionary.Helper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryFolder
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryItem

@Database(entities = [DictionaryFolder::class,DictionaryItem::class], version = 1, exportSchema = false)
abstract class DictionaryHelper : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDAO

    companion object {
        private var INSTANCE: DictionaryHelper? = null
        fun getDataBase(context: Context): DictionaryHelper {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance;
            }
            synchronized(this) {
                val intance = Room.databaseBuilder(
                    context.applicationContext,
                    DictionaryHelper::class.java,
                    name = "dictionary_database",
                ).build()
                INSTANCE=intance
                return intance
            }
        }
    }

}