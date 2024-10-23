package com.tobe.carbonnewsapp.navigation

sealed class Routes(val route: String, val label: String) {
    object News : Routes("news", "News")
    object Saved : Routes("saved", "Saved")
}