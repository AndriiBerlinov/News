package com.news.screens

import android.content.Context
import android.content.Intent
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
import com.news.db.News
import com.news.db.getFavoritesFromDatabase
import com.news.db.removeFromFavorites

@Composable
fun DetailFavoriteScreen(newsApiClient: NewsApiClient, title: String?, context: Context) {
    title ?: return
    var favoriteNews by remember { mutableStateOf<News?>(null) }

    // Получение избранной новости из базы данных
    LaunchedEffect(true) {
        val favorites = getFavoritesFromDatabase(context)
        favoriteNews = favorites.find { it.title == title }
    }

    // Если избранная новость найдена, отобразить ее информацию
    favoriteNews?.let { news ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp)
            ) {
                // Кнопки для добавления в избранное и для передачи информации
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            removeFromFavorites(context, news.title)
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Remove from Favorites")
                    }
                    Button(
                        onClick = {
                            val textToShare = "${news.title}\n${news.url}"
                            shareText(context, textToShare)
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Forward")
                    }
                }
            }
            // Отображение информации о новости
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 95.dp)
            ) {
                item {
                    Text(
                        text = news.title,
                        style = TextStyle(
                            fontSize = 26.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                    )
                    AsyncImage(
                        model = news.urlToImage,
                        contentDescription = null,
                        modifier = Modifier
                            .size(400.dp)
                            .padding(start = 12.dp)
                    )
                    Text(
                        text = news.description,
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                    )
                    Spacer(modifier = Modifier.size(18.dp))
                    Text(
                        text = news.content,
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                    )
                    Spacer(modifier = Modifier.size(18.dp))
                    Text(
                        text = news.publishedAt,
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                    )
                    Spacer(modifier = Modifier.size(18.dp))
                    Text(
                        text = news.url,
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