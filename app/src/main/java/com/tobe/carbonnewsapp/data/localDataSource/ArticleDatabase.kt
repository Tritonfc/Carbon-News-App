package com.tobe.newsapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tobe.carbonnewsapp.data.localDataSource.ArticleDao
import com.tobe.carbonnewsapp.data.localDataSource.Converters
import com.tobe.carbonnewsapp.data.models.Article


@Database(
    entities = [
        Article :: class
    ],
    version = 1,

    exportSchema = false
)

@TypeConverters(
    Converters :: class
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao


    companion object{
        private var instance : ArticleDatabase? = null
        private var LOCK = Any()


        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabse(context).also { instance = it }

        }

        private fun createDatabse(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "ArticleDB.db"
            ).build()
    }
}