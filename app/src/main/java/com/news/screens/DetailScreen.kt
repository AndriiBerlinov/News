package com.news.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.Article
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import com.news.db.addToFavorites

@Composable
fun DetailScreen(newsApiClient: NewsApiClient, title: String?, context: Context) {
    title ?: return
    var sourcesList by remember { mutableStateOf<List<Article>>(emptyList()) }

    LaunchedEffect(true) {
        newsApiClient.getTopHeadlines(
            TopHeadlinesRequest.Builder()
                .q(title)
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
                .height(60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        sourcesList.forEach { article ->
                            addToFavorites(context, article.title, article.urlToImage, article.description, article.content, article.publishedAt, article.url)
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Add to Favorites")
                }
                Button(
                    onClick = {
                        val textToShare = buildString {
                            sourcesList.forEach { article ->
                                append("${article.title}\n")
                                append("${article.url}\n")
                            }
                        }
                        shareText(context, textToShare)
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Forward")
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 95.dp)
        ) {
            items(sourcesList) { source ->
                Column {
                    Text(
                        text = source.title ?: "",
                        style = TextStyle(
                            fontSize = 26.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                    )
                    AsyncImage(
                        model = source.urlToImage ?: "",
                        contentDescription = null,
                        modifier = Modifier
                            .size(400.dp)
                            .padding(start = 12.dp)
                    )
                    Text(
                        text = source.description ?: "",
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                    )
                    Spacer(modifier = Modifier.size(18.dp))
                    Text(
                        text = source.content ?: "",
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                    )
                    Spacer(modifier = Modifier.size(18.dp))
                    Text(
                        text = source.publishedAt ?: "",
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                    )
                    Spacer(modifier = Modifier.size(18.dp))
                    Text(
                        text = source.url ?: "",
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                    )
                }
            }
        }
    }
}

private fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
}