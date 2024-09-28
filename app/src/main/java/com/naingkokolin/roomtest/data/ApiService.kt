package com.naingkokolin.roomtest.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object NetworkModule {

    private const val BASE_URL = "https://newsapi.org/v2/"

    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

    fun provideRetrofitApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}

interface ApiService {
    @GET("everything?q=keyword&apiKey=093295af6d1146018b549e0443ffe80c")
    suspend fun getNews(): NewsApiResponse
}

// interface ApiService {
//    @GET("everything")
//    suspend fun getNews(
//        @retrofit2.http.Query("q") keyword: String,
//        @retrofit2.http.Query("apiKey") apiKey: String
//    ): List<News>
//}