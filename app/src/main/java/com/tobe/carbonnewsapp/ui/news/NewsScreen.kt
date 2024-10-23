package com.tobe.carbonnewsapp.ui.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.tobe.carbonnewsapp.data.models.Article
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview

import  coil3.compose.rememberAsyncImagePainter
import com.tobe.carbonnewsapp.data.models.Source


@Composable
fun NewsScreen(
    articles: List<Article>,
    onSearch: (String) -> Unit,
    onBookmarkClicked: (Article) -> Unit, // Add bookmark handler
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = modifier.fillMaxSize()) {

        SearchBar(
            query = searchQuery,
            onQueryChanged = { query ->
                searchQuery = query
                onSearch(query.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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

@Composable
fun NewsCard(
    article: Article,
    onBookmarkClicked: (Article) -> Unit, // To handle bookmark clicks
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // News Image
            if (!article.urlToImage.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(model = article.urlToImage),
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title, Author and Bookmark Icon in Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title and Author in a Column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "By ${article.author}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Bookmark Icon
                IconButton(onClick = { onBookmarkClicked(article) }) {
                    Icon(
                        imageVector = Icons.Outlined.Star, // Use default bookmark icon
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun NewsCardPreview() {
    val article = Article(
        id = 1,
        author = "John Doe",
        content = "Some content about the news article.",
        description = "A brief description.",
        publishedAt = "2022-10-11",
        source = Source(id = "1", name = "Source Name"),
        title = "Breaking News: Compose is Awesome!",
        url = "https://news.example.com",
        urlToImage = "https://images.pexels.com/photos/104827/cat-pet-animal-domestic-104827.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
    )

    NewsCard(article = article, onBookmarkClicked = { bookmarkedArticle ->
        // Handle bookmark click, this is where you will later save the article
        println("Bookmarked: ${bookmarkedArticle.title}")
    })
}

