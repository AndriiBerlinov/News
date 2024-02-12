package com.news.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.news.db.News
import com.news.db.getFavoritesFromDatabase

@Composable
fun FavoritesScreen(navController: NavController) {
    val context = LocalContext.current
    val favorites = getFavoritesFromDatabase(context)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(60.dp)
        ) {
            Text(
                text = "Favorites News:",
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
                .padding(top = 60.dp)
        ) {
            items(favorites) { favorite ->
                FavoriteNewsItem(navController, news = favorite)
            }
        }
    }
}

@Composable
fun FavoriteNewsItem(navController: NavController, news: News) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(news.urlToImage),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .padding(start = 12.dp)
                .clickable {
                    navController.navigate("detailFavoriteScreen/${news.title}")
                }
        )
        Text(
            text = news.title ?: "",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Blue
            ),
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .clickable {
                    navController.navigate("detailFavoriteScreen/${news.title}")
                }
        )
    }
}