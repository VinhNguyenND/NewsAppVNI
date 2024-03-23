package com.example.myappnews.Data.Local.Article

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myappnews.Data.Model.Article.Article

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(article: ArticleEntity)

    @Query("DELETE FROM Articles")
    suspend fun deleteAllArticles()

    @Query("SELECT * FROM Articles")
    fun getAllArticle(): LiveData<List<ArticleEntity>>
}