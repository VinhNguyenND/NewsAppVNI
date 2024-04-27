package com.example.myappnews.Data.Local.Article.Down

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface ArticleLDownDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun DownArticle(article: ArticleDownEntity):Long

    @Query("DELETE FROM ArticlesDown")
    suspend fun disDownAllArticles()
    @Query("DELETE FROM ArticlesDown WHERE idArticle=:id")
    suspend fun deleteDownArticle(id:String)

    @Query("SELECT * FROM ArticlesDown")
    fun getAllDownArticle(): LiveData<List<ArticleDownEntity>>
}