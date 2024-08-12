package com.example.cryptocurrencytraineetest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytraineetest.model.CryptocurrencyWithDescription
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
    val isUSD = repository.isUSD
    val idCurrency = repository.idCurrency


    fun createEvent(e: Event) {
        onEvent(e)
    }

    private fun onEvent(e: Event) {
        when (e) {
            is Event.LoadListOfCryptocurrencies -> {
                repository.isUSD.value = true
                getCryptocurrencies("usd")
            }

            is Event.ReloadListOfCryptocurrencies -> {
                repository.isUSD.value = e.currency == "usd"
                getCryptocurrencies(e.currency)
            }

            is Event.LoadCryptCard -> {
                repository.idCurrency.value = e.id
                getCryptocurrencyByName(repository.idCurrency.value)
            }
        }
    }

    private fun getCryptocurrencies(vsCurrency: String) =
        viewModelScope.launch {
            repository.getCryptocurrencies(vsCurrency, perPage).onStart {
                repository.response.value = ApiState.Loading
            }.catch {
                repository.response.value = ApiState.Failure
            }.collect {
                repository.response.value = ApiState.SuccessLoadingList(it)
            }
        }

    private fun getCryptocurrencyByName(id: String) =
        viewModelScope.launch {
            repository.getCryptocurrencyByName(id).onStart {
                repository.response.value = ApiState.Loading
            }.catch {
                repository.response.value = ApiState.Failure

            }.collect {
                repository.response.value = ApiState.SuccessLoadingCoin(it)
            }
        }
}

sealed class Event {
    data object LoadListOfCryptocurrencies : Event()
    data class ReloadListOfCryptocurrencies(val currency: String) : Event()
    data class LoadCryptCard(val id: String) : Event()
}

sealed class ApiState {
    class SuccessLoadingList(val data: List<Cryptocurrency>) : ApiState()
    class SuccessLoadingCoin(val data: CryptocurrencyWithDescription) : ApiState()

    data object Loading : ApiState()
    data object Empty : ApiState()
    data object Failure : ApiState()
}
