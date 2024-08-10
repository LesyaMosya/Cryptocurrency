package com.example.cryptocurrencytraineetest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytraineetest.model.CoinDescription
import com.example.cryptocurrencytraineetest.model.Cryptocurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptViewModel @Inject constructor(
    private val repository: CryptRepository
) : ViewModel() {

    private val perPage = 30
    val response = repository.response

    init {
        if (repository.isMainPage.value)
            getCryptocurrencies("usd")
    }

    fun getCryptocurrencies(vsCurrency: String) =
        viewModelScope.launch {
            repository.getCryptocurrencies(vsCurrency, perPage).onStart {
                repository.response.value = ApiState.Loading
            }.catch {
                repository.response.value = ApiState.Failure(it)
            }.collect {
                repository.response.value = ApiState.SuccessLoadingList(it)
            }
        }

    fun getCryptocurrencyByName(id: String) =
        viewModelScope.launch {
            repository.isMainPage.value = false

            repository.getCryptocurrencyByName(id).onStart {
                repository.response.value = ApiState.Loading
            }.catch {
                repository.response.value = ApiState.Failure(it)

            }.collect{
                repository.response.value = ApiState.SuccessLoadingCoin(it)
                repository.isMainPage.value = true
            }
        }
}


sealed class ApiState {
    class SuccessLoadingList(val data: List<Cryptocurrency>) : ApiState()
    class SuccessLoadingCoin(val data: CoinDescription) : ApiState()

    class Failure(val msg: Throwable) : ApiState()
    data object Loading: ApiState()
    data object Empty: ApiState()
}