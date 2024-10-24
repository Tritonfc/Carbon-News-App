package com.tobe.carbonnewsapp.ui.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.tobe.carbonnewsapp.R
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.tobe.carbonnewsapp.data.models.Article
import com.tobe.carbonnewsapp.util.Resource

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel(),
    onBookmarkClicked: (Article) -> Unit // Add bookmark handler
) {
    // Collecting the breaking news and search news flow states
    val breakingNewsState by viewModel.breakingNewsFlow.collectAsStateWithLifecycle()
    val searchNewsState by viewModel.searchNewsFlow.collectAsStateWithLifecycle()

    // Combine breaking news and searched news into one list for display
    val articles = when {
        searchNewsState is Resource.Success -> {
            searchNewsState.data // Return search results if available
        }
        breakingNewsState is Resource.Success -> {
            breakingNewsState.data // Return breaking news if search results are empty
        }
        else -> {
            emptyList() // Fallback to an empty list if neither state is successful
        }
    }

    NewsScreenContent(
        articles = articles,
        onSearch = { viewModel.updateSearchQuery(it) },
        onBookmarkClicked = onBookmarkClicked,
        modifier = modifier,
        breakingNewsState = breakingNewsState,
        searchNewsState = searchNewsState
    )
}

@Composable
fun NewsScreenContent(
    articles: List<Article>?,
    onSearch: (String) -> Unit,
    breakingNewsState: Resource<List<Article>>,
    searchNewsState: Resource<List<Article>>,
    onBookmarkClicked: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("") )}

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        SearchBar(
            query = searchQuery,
            onQueryChanged = { query ->
                searchQuery = query
                onSearch(query.text) // Call the onSearch with the query
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        when {
            breakingNewsState is Resource.Loading || searchNewsState is Resource.Loading -> {
                // Show a loading indicator while fetching news
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            breakingNewsState is Resource.Error -> {
                // Show an error message for breaking news
                Text(
                    text = breakingNewsState.message ?: "Unknown error occurred",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            searchNewsState is Resource.Error -> {
                // Show an error message for search news
                Text(
                    text = searchNewsState.message ?: "Unknown error occurred",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                if(articles.isNullOrEmpty()){
                    Text(
                        text = "No news avaialble",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }else {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(articles) { article ->
                            NewsCard(
                                article = article,
                                onBookmarkClicked = onBookmarkClicked,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        decorationBox = { innerTextField ->
            if (query.text.isEmpty()) {
                Text(text = "Search news...", color = Color.Gray)
            }
            innerTextField()
        }
    )
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsCard(
    article: Article,
    modifier: Modifier = Modifier,
    isBookmarked: Boolean = false,
    onBookmarkClicked: (Article) -> Unit = {}
) {
    Card(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // News Image
            if (!article.urlToImage.isNullOrEmpty()) {

                GlideImage(
                    model = article.urlToImage,
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),


                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row containing the title, author, and bookmark icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Column for Title and Author
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    // News Title
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Author
                    Text(
                        text = "By ${article.author}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Bookmark Icon
                IconButton(
                    onClick = { onBookmarkClicked(article) }
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = if (isBookmarked) "Remove Bookmark" else "Add Bookmark"
                    )
                }
            }
        }
    }
}
