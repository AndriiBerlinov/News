package com.news.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavController
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.request.SourcesRequest
import com.kwabenaberko.newsapilib.models.response.SourcesResponse

@Composable
fun ScreenSources(newsApiClient: NewsApiClient, navController: NavController) {
    var sources by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(true) {
        newsApiClient.getSources(
            SourcesRequest.Builder()
                .language("en")
                .country("us")
                .build(),
            object : NewsApiClient.SourcesCallback {
                override fun onSuccess(response: SourcesResponse) {
                    sources = response.sources.map { it.name }
                }
                override fun onFailure(throwable: Throwable) {
                    Log.e("NewsApiClient", "Failed to fetch sources", throwable)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp)
        ) {
            items(sources) { source ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = source,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                            .clickable {
                                navController.navigate("newsBySourcesScreen/$source")
                            }
                    )
                }
            }
        }
    }
}