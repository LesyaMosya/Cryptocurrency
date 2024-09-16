package com.example.cryptocurrencytraineetest.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService) {


    fun getCryptocurrencies(vsCurrency: String, perPage : Int) = flow {
        emit(apiService.getCryptocurrencies(vsCurrency, perPage))
    }.flowOn(Dispatchers.IO)

    fun getCryptocurrencyByName(id: String) = flow {
        emit(apiService.getCryptocurrencyByName(id))
    }.flowOn(Dispatchers.IO)
}