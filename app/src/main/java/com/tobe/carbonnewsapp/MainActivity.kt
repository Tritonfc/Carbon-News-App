package com.tobe.carbonnewsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tobe.carbonnewsapp.data.models.Article
import com.tobe.carbonnewsapp.navigation.News
import com.tobe.carbonnewsapp.navigation.Saved
import com.tobe.carbonnewsapp.navigation.ViewArticle
import com.tobe.carbonnewsapp.ui.article.ArticleScreen
import com.tobe.carbonnewsapp.ui.news.NewsScreen
import com.tobe.carbonnewsapp.ui.saved.SavedScreen
import com.tobe.carbonnewsapp.ui.theme.CarbonNewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            CarbonNewsAppTheme {
                // Main screen content with scaffold
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    // NavHost for handling navigation between screens
                    NavigationHost(navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}



// Bottom navigation component
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomNavItems = listOf<BottomNavItem>(
        BottomNavItem(
            title = "News",
            selectedIcon = Icons.Filled.Home,

            route = News
        ),
        BottomNavItem(
            title = "Saved",
            selectedIcon = Icons.Filled.Star,

            route = Saved
        ),

    )

    NavigationBar {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.selectedIcon,
                        contentDescription = null
                    )

                },
                label = { Text(screen.title) },
                selected = navController.currentDestination?.route == screen.route,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}

// NavHost for handling different routes/screens
@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = News) {
        composable<News> {
            NewsScreen(onArticleClicked = { article->
                val articleJson = Json.encodeToString(article)
navController.navigate(route = ViewArticle(articleJson))
            })
        }

        composable<Saved> {
            SavedScreen( onArticleClicked = { article->
                val articleJson = Json.encodeToString(article)
                navController.navigate(route = ViewArticle(articleJson))

            })
        }

        composable<ViewArticle> { backStackEntry ->
            val articleJson: ViewArticle = backStackEntry.toRoute()
            val articleObj = Json.decodeFromString<Article>(articleJson.article)
            ArticleScreen(article = articleObj)


        }


    }
}




@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    CarbonNewsAppTheme {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            NavigationHost(navController, modifier = Modifier.padding(it))
        }
    }
}
data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val route: Any
)