package com.example.myappnews.Data.Local.Article.History

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity)

    @Query("DELETE FROM Articles")
    suspend fun deleteAllArticles()

    @Query("SELECT * FROM Articles")
    fun getAllArticle(): LiveData<List<ArticleEntity>>
}