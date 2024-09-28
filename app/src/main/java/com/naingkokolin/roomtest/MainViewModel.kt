package com.naingkokolin.roomtest

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naingkokolin.roomtest.data.Article
import com.naingkokolin.roomtest.data.NetworkModule
import com.naingkokolin.roomtest.data.NewsApiResponse
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _newsState = mutableStateOf(NewsState())
    val newsState: MutableState<NewsState> = _newsState

    init {
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch {
            try {
                _newsState.value = _newsState.value.copy(
                    loading = false,
                    news = NetworkModule.provideRetrofitApiService(NetworkModule.provideRetrofit()).getNews().articles
                )
            } catch (e: Exception) {
                _newsState.value = _newsState.value.copy(
                    loading = false,
                    error = "Error while fetching data, Error Message: ${e.message}"
                )
            }
        }
    }


    data class NewsState (
        val loading: Boolean = true,
        val error: String? = null,
        val news: List<Article> = emptyList()
    )
}