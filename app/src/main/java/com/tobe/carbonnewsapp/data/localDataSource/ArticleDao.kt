package com.tobe.carbonnewsapp.data.localDataSource

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tobe.carbonnewsapp.data.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(article: Article)

     @Query("SELECT * FROM articles")
     suspend fun getAllArticles():List<Article>

     @Delete
     suspend fun delete(article: Article)
}