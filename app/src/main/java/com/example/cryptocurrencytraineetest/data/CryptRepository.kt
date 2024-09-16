package com.example.cryptocurrencytraineetest.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.cryptocurrencytraineetest.data.api.ApiHelper
import com.example.cryptocurrencytraineetest.data.model.CryptocurrencyWithDescription
import com.example.cryptocurrencytraineetest.data.model.Cryptocurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptRepository @Inject constructor(private val apiHelper: ApiHelper) {
    val response: MutableState<ApiState> = mutableStateOf(ApiState.Empty)
    val isUSD = mutableStateOf(true)
    val idCurrency = mutableStateOf("")

    private val perPage = 30
    suspend fun getCryptocurrencies(vsCurrency: String) {
        apiHelper.getCryptocurrencies(vsCurrency, perPage)
            .flowOn(Dispatchers.IO)
            .catch {
                response.value = ApiState.Failure
            }
            .collect{
                response.value = ApiState.SuccessLoadingList(it)
            }
    }

    suspend fun getCryptocurrencyByName(id : String) {
        apiHelper.getCryptocurrencyByName(id)
            .flowOn(Dispatchers.IO)
            .catch {
                response.value = ApiState.Failure
            }
            .collect {
                response.value = ApiState.SuccessLoadingCoin(it)
            }
    }


}

sealed class ApiState {
    class SuccessLoadingList(val data: List<Cryptocurrency>) : ApiState()
    class SuccessLoadingCoin(val data: CryptocurrencyWithDescription) : ApiState()

    data object Loading : ApiState()
    data object Empty : ApiState()
    data object Failure : ApiState()
}