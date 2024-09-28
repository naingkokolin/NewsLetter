package com.naingkokolin.roomtest.data

import android.content.Context
import android.os.Parcelable
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
): Parcelable

@Parcelize
data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
): Parcelable

@Parcelize
data class Source(
    val id: String?,
    val name: String
): Parcelable

val Context.dataStore by preferencesDataStore(name = "article_prefs")

val FAV_KEY = booleanPreferencesKey("is_favorite")

suspend fun saveFavoriteState(context: Context, isFavorite: Boolean) {
    context.dataStore.edit {
        it[FAV_KEY] = isFavorite
    }
}

fun getFavoriteState(context: Context): Flow<Boolean> {
    return context.dataStore.data.map {
        it[FAV_KEY] ?: false
    }
}