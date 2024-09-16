package com.example.cryptocurrencytraineetest.data.api

import com.example.cryptocurrencytraineetest.data.model.CryptocurrencyWithDescription
import com.example.cryptocurrencytraineetest.data.model.Cryptocurrency
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers("x-cg-demo-api-key: CG-QS9uxxeV2SPizHgzQC3Y8tyK")
    @GET("coins/markets")
    suspend fun getCryptocurrencies(
        @Query("vs_currency") vsCurrency: String,
        @Query("per_page") perPage: Int
    ): List<Cryptocurrency>

    @Headers("x-cg-demo-api-key: CG-QS9uxxeV2SPizHgzQC3Y8tyK")
    @GET("coins/{id}")
    suspend fun getCryptocurrencyByName(
        @Path("id") id: String
    ): CryptocurrencyWithDescription
}