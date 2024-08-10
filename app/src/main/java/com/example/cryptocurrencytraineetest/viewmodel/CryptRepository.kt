package com.example.cryptocurrencytraineetest.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.cryptocurrencytraineetest.api.ApiService
import com.example.cryptocurrencytraineetest.model.CoinDescription
import com.example.cryptocurrencytraineetest.model.Cryptocurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptRepository @Inject constructor(private val apiService: ApiService) {
    fun getCryptocurrencies(vsCurrency: String, perPage: Int): Flow<List<Cryptocurrency>> = flow {
        emit(apiService.getCryptocurrencies(vsCurrency, perPage))
    }.flowOn(Dispatchers.IO)

    fun getCryptocurrencyByName(id: String) : Flow<CoinDescription> = flow {
        emit(apiService.getCryptocurrencyByName(id))
    }.flowOn(Dispatchers.IO)

    val response: MutableState<ApiState> = mutableStateOf(ApiState.Empty)
    val isMainPage = mutableStateOf(true)
}