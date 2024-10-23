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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tobe.carbonnewsapp.navigation.Routes
import com.tobe.carbonnewsapp.ui.news.NewsScreen
import com.tobe.carbonnewsapp.ui.saved.SavedScreen
import com.tobe.carbonnewsapp.ui.theme.CarbonNewsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    val items = listOf(
        Routes.News,
        Routes.Saved
    )

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        is Routes.News -> Icon(Icons.Filled.Home, contentDescription = screen.label)
                        is Routes.Saved -> Icon(Icons.Filled.Star, contentDescription = screen.label)
                    }
                },
                label = { Text(screen.label) },
                selected = navController.currentDestination?.route == screen.route,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}

// NavHost for handling different routes/screens
@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Routes.News.route) {
        addNewsComposable(navController)
        addSavedComposable(navController)
    }
}

// News Screen composable
fun NavGraphBuilder.addNewsComposable(navController: NavHostController) {
    composable(Routes.News.route) {
        NewsScreen(articles = emptyList(), onSearch = {

        }, onBookmarkClicked = {

        })
    }
}

// Saved Screen composable
fun NavGraphBuilder.addSavedComposable(navController: NavHostController) {
    composable(Routes.Saved.route) {
        SavedScreen()
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
