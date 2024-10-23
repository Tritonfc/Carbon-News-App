package com.tobe.carbonnewsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tobe.carbonnewsapp.data.models.Article
import com.tobe.carbonnewsapp.data.repository.NewsRepository
import com.tobe.carbonnewsapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
class NewsViewModel @Inject constructor (private val newsRepository: NewsRepository) : ViewModel() {

    // Search News State
    private val _searchNewsState = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
    val searchNewsState: StateFlow<Resource<List<Article>>> = _searchNewsState.asStateFlow()

    // Search Query State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        // Observe search query changes to trigger news search
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collect { query ->
                    searchNews(query) // Perform the search when a query is entered
                }
        }
    }

    // Creating a flow for breaking news, which will only fetch data when subscribed to
    val breakingNewsFlow: StateFlow<Resource<List<Article>>> = flow {
        emit(Resource.Loading()) // Emit loading state initially
        try {
            val response = newsRepository.getBreakingNews("us", 1)
            if (response.isSuccessful) {
                response.body()?.let { newsResponse ->
                    emit(Resource.Success(newsResponse.articles))
                } ?: run {
                    emit(Resource.Error("No news found"))
                }
            } else {
                emit(Resource.Error("Failed to fetch news"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

    // Function to search news based on query
    private fun searchNews(query: String, pageNumber: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchNewsState.value = Resource.Loading() // Set loading state
            try {
                val response = newsRepository.searchNews(query, pageNumber)
                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        _searchNewsState.value = Resource.Success(newsResponse.articles)
                    } ?: run {
                        _searchNewsState.value = Resource.Error("No news found for \"$query\"")
                    }
                } else {
                    _searchNewsState.value = Resource.Error("Failed to search news")
                }
            } catch (e: Exception) {
                _searchNewsState.value = Resource.Error(e.message ?: "An error occurred while searching")
            }
        }
    }


    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
