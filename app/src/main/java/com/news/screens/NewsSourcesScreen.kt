package com.news.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.Article
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse

@Composable
fun NewsSourcesScreen(newsApiClient: NewsApiClient, navController: NavHostController) {
    var sourcesList by remember { mutableStateOf<List<Article>>(emptyList()) }
    val selectedArticle = remember { mutableStateOf<Article?>(null) }

    LaunchedEffect(true) {
        newsApiClient.getTopHeadlines(
            TopHeadlinesRequest.Builder()
                .language("en")
                .build(),
            object : NewsApiClient.ArticlesResponseCallback {
                override fun onSuccess(response: ArticleResponse) {
                    sourcesList = response.articles
                }
                override fun onFailure(throwable: Throwable) {
                    Log.e("NewsApiClient", "Failed to fetch top headlines", throwable)
                }
            }
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(95.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        navController.navigate("intermediateSearchScreen")
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Search")
                }

                Button(
                    onClick = {
                        navController.navigate("ScreenSources")
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Sources")
                }

                Button(
                    onClick = {
                        navController.navigate("FavoritesScreen")
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Favorites")
                }
            }
            Text(
                text = "Hot News:",
                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Blue
                )
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 95.dp)
        ) {
            items(sourcesList) { source ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberImagePainter(source.urlToImage),
                        contentDescription = null,
                        modifier = Modifier
                            .size(160.dp)
                            .padding(start = 12.dp)
                            .clickable {
                                selectedArticle.value = source
                            },
                    )
                    Text(
                        text = source.title ?: "",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                            .clickable {
                                selectedArticle.value = source
                            }
                    )
                }
            }
        }
    }

    selectedArticle.value?.let { article ->
        article.title?.let { title ->
            navController.navigate("detailScreen/$title")
        }
    }
}