package com.example.cryptocurrencytraineetest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytraineetest.data.CryptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptViewModel @Inject constructor(
    private val repository: CryptRepository
) : ViewModel() {

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
            repository.getCryptocurrencies(vsCurrency)
        }

    private fun getCryptocurrencyByName(id: String) =
        viewModelScope.launch {
            repository.getCryptocurrencyByName(id)
        }
}

sealed class Event {
    data object LoadListOfCryptocurrencies : Event()
    data class ReloadListOfCryptocurrencies(val currency: String) : Event()
    data class LoadCryptCard(val id: String) : Event()
}
