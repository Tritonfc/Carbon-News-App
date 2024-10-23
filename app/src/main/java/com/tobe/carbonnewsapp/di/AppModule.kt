package com.tobe.newsapp.di

import android.content.Context
import com.tobe.carbonnewsapp.data.remoteDataSource.api.NewsApi

import com.tobe.newsapp.data.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()



    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi :: class.java)


    @Singleton
    @Provides
    fun provideRoomDb(@ApplicationContext appContext: Context): ArticleDatabase {
        return ArticleDatabase(appContext)

    }

    @Singleton
    @Provides
    fun provideDao(db: ArticleDatabase) = db.getArticleDao()


}