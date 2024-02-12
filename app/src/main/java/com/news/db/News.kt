package com.news.db

data class News(
    val title: String,
    val urlToImage: String,
    val description: String,
    val content: String,
    val publishedAt: String,
    val url: String
)