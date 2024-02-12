package com.news.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kwabenaberko.newsapilib.NewsApiClient
import com.news.screens.DetailFavoriteScreen
import com.news.screens.IntermediateSearchScreen
import com.news.screens.ScreenSources
import com.news.screens.SearchScreen
import com.news.screens.DetailScreen
import com.news.screens.FavoritesScreen
import com.news.screens.NewsBySourcesScreen
import com.news.screens.NewsSourcesScreen
import com.news.utilites.API_KEY

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val newsApiClient = remember { NewsApiClient(API_KEY) }

    NavHost(navController, startDestination = "sourcesScreen") {
        composable("sourcesScreen") {
            NewsSourcesScreen(newsApiClient, navController)
        }
        composable("detailScreen/{title}",
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) {
            val title = it.arguments?.getString("title")
            DetailScreen(newsApiClient, title, context = LocalContext.current)
        }
        composable("detailFavoriteScreen/{title}",
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) {
            val title = it.arguments?.getString("title")
            DetailFavoriteScreen(newsApiClient, title, context = LocalContext.current)
        }
        composable("searchScreen/{query}",
            arguments = listOf(
                navArgument("query") { type = NavType.StringType }
            )) {
            val query = it.arguments?.getString("query")
            query?.let { searchText ->
                SearchScreen(newsApiClient, navController, searchText)
            }
        }
        composable("intermediateSearchScreen") {
            IntermediateSearchScreen(navController = navController)
        }
        composable("ScreenSources") {
            ScreenSources(newsApiClient, navController)
        }
        composable("FavoritesScreen") {
            FavoritesScreen(navController)
        }
        composable("newsBySourcesScreen/{sourceName}",
            arguments = listOf(
                navArgument("sourceName") { type = NavType.StringType }
            )
        ) {
            val sourceName = it.arguments?.getString("sourceName")
            NewsBySourcesScreen(newsApiClient, navController, sourceName)
        }
    }
}