package com.tobe.carbonnewsapp.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tobe.carbonnewsapp.data.models.Article
import com.tobe.carbonnewsapp.data.repository.NewsRepository
import com.tobe.carbonnewsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class NewsViewModel @Inject constructor (private val newsRepository: NewsRepository) : ViewModel() {
private val _searchNewsFlow = MutableStateFlow<Resource<List<Article>>>(Resource.Empty())
    val searchNewsFlow = _searchNewsFlow.asStateFlow()



    private val _searchQuery = MutableStateFlow("")


    init {

        viewModelScope.launch(Dispatchers.IO) {
            _searchQuery
                .debounce(300) // Wait for 300ms after the user stops typing
                .distinctUntilChanged()
                .filter {
                    it.isNotBlank()

                }
                .collect { query ->
                    searchNews(query)
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
fun saveArticleOffline(article: Article){
    viewModelScope.launch(Dispatchers.IO){
        newsRepository.insertNewsArticle(article)
    }

}

   private suspend fun searchNews(query: String, pageNumber: Int = 1) {
        _searchNewsFlow.value = Resource.Loading() // Emit loading state
        try {
            val response = newsRepository.searchNews(query, 1) // Fetch the news
            if (response.isSuccessful) {
                response.body()?.let { newsResponse ->
                    _searchNewsFlow.value =
                        Resource.Success(newsResponse.articles) // Emit success with articles
                }
            } else {
                _searchNewsFlow.value = Resource.Error("Failed to fetch news for \"$query\"")
            }
        } catch (e: Exception) {
            _searchNewsFlow.value =
                Resource.Error(e.message ?: "An error occurred while searching")
        }
    }

        fun updateSearchQuery(query: String) {
            if (query.isBlank()) {
                _searchNewsFlow.value = Resource.Empty()
            } else {
                _searchQuery.value = query
            }

        }

}
