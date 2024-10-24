package com.tobe.carbonnewsapp.ui.saved

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tobe.carbonnewsapp.data.models.Article
import com.tobe.carbonnewsapp.ui.news.NewsCard
import com.tobe.carbonnewsapp.ui.news.NewsViewModel
import com.tobe.carbonnewsapp.util.Resource

// Sample Saved screen content
@Composable
fun SavedScreen(  viewModel: SavedArticlesViewModel = hiltViewModel(),modifier: Modifier= Modifier,onArticleClicked:(Article)->Unit) {

    val savedNewsState by viewModel.savedNewsFlow.collectAsStateWithLifecycle()

    Box(
        modifier= modifier
            .fillMaxSize(),

contentAlignment = Alignment.Center
    ) {

        when (savedNewsState) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            is Resource.Success -> {
                val articles = savedNewsState.data

                if (articles.isNullOrEmpty()) {
                    Text(
                        text = "No news avaialble",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {

                        items(articles) { article ->
                            NewsCard(
                                article = article,
                                onArticleClicked = onArticleClicked,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }


                }
            }

            is Resource.Error -> {
                Text(
                    text = savedNewsState.message ?: "Unknown error occurred",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {

            }
        }
    }

}