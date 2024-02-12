package com.news.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Favorites", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS favorites (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, urlToImage TEXT, description TEXT, content TEXT, publishedAt TEXT, url TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS favorites")
        onCreate(db)
    }

    fun addFavorite(title: String, urlToImage: String, description: String, content: String, publishedAt: String, url: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("title", title)
        values.put("urlToImage", urlToImage)
        values.put("description", description)
        values.put("content", content)
        values.put("publishedAt", publishedAt)
        values.put("url", url)
        db.insert("favorites", null, values)
        db.close()
    }
}

fun addToFavorites(context: Context, title: String?, urlToImage: String?, description: String?, content: String?, publishedAt: String?, url: String?) {
    if (title != null && urlToImage != null && description != null && content != null && publishedAt != null && url != null) {
        val dbHelper = DatabaseHelper(context)
        dbHelper.addFavorite(title, urlToImage, description, content, publishedAt, url)
    }
}

fun getFavoritesFromDatabase(context: Context): List<News> {
    val dbHelper = DatabaseHelper(context)
    val db = dbHelper.readableDatabase
    val favorites = mutableListOf<News>()
    val cursor = db.rawQuery("SELECT * FROM favorites", null)
    while (cursor.moveToNext()) {
        val title = cursor.getString(cursor.getColumnIndex("title"))
        val urlToImage = cursor.getString(cursor.getColumnIndex("urlToImage"))
        val description = cursor.getString(cursor.getColumnIndex("description"))
        val content = cursor.getString(cursor.getColumnIndex("content"))
        val publishedAt = cursor.getString(cursor.getColumnIndex("publishedAt"))
        val url = cursor.getString(cursor.getColumnIndex("url"))
        favorites.add(News(title, urlToImage, description, content, publishedAt, url))
    }
    cursor.close()
    db.close()
    return favorites
}

fun removeFromFavorites(context: Context, title: String) {
    val dbHelper = DatabaseHelper(context)
    val db = dbHelper.writableDatabase
    db.delete("favorites", "title = ?", arrayOf(title))
    db.close()
}