package com.example.matchcast.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface EplApiService {
    @GET("feed/json/epl-2023")
     suspend fun getMatches(): List<MatchDto>
}

object RetrofitClient{
    private const val BASE_URL = "https://fixturedownload.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: EplApiService by lazy {
        retrofit.create(EplApiService::class.java)
    }
}