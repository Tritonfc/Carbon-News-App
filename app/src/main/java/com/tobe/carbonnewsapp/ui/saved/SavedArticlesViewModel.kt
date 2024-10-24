package com.tobe.carbonnewsapp.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tobe.carbonnewsapp.data.models.Article
import com.tobe.carbonnewsapp.data.repository.NewsRepository
import com.tobe.carbonnewsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class SavedArticlesViewModel@Inject constructor (private val newsRepository: NewsRepository) : ViewModel()
{

    val savedNewsFlow: StateFlow<Resource<List<Article>>> = flow {
        emit(Resource.Loading()) // Emit loading state initially
        try {
            val response = newsRepository.fetchOfflineArticles()


                emit(Resource.Success(response))



        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

}