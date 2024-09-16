package com.example.cryptocurrencytraineetest.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Snackbar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cryptocurrencytraineetest.ui.util.DecimalFormatter
import com.example.cryptocurrencytraineetest.R
import com.example.cryptocurrencytraineetest.data.model.Cryptocurrency
import com.example.cryptocurrencytraineetest.data.ApiState
import com.example.cryptocurrencytraineetest.ui.viewmodel.CryptViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.example.cryptocurrencytraineetest.ui.screen.destinations.ExtraInformationScreenDestination
import com.example.cryptocurrencytraineetest.ui.util.Failure
import com.example.cryptocurrencytraineetest.ui.util.Loading
import com.example.cryptocurrencytraineetest.ui.viewmodel.Event
import kotlinx.coroutines.launch
import kotlin.text.Typography.dollar

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun CryptListScreen(
    navigator: DestinationsNavigator,
    viewModel: CryptViewModel = hiltViewModel()
) {
    viewModel.createEvent(Event.LoadListOfCryptocurrencies)

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            }
        },
        topBar = {
            Surface(
                shadowElevation = 5.dp
            ) {
                TopAppBar(
                    title = { TopAppBar(viewModel = viewModel) },
                    expandedHeight = 130.dp,
                    modifier = Modifier
                        .padding(15.dp, 0.dp)
                )
            }
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            CryptList(viewModel, navigator, snackbarHostState)
        }
    }
}

@Composable
fun TopAppBar(viewModel: CryptViewModel) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = stringResource(id = R.string.crypt_list_str),
            style = MaterialTheme.typography.titleMedium
        )
        Row {
            CurrencyChip(
                viewModel = viewModel,
                currency = stringResource(id = R.string.currency_usd_str)
            )
            Spacer(modifier = Modifier.width(15.dp))
            CurrencyChip(
                viewModel = viewModel,
                currency = stringResource(id = R.string.currency_rub_str)
            )
        }
    }
}

@Composable
fun CurrencyChip(
    viewModel: CryptViewModel, currency: String
) {
    FilterChip(onClick = {
        viewModel.createEvent(Event.ReloadListOfCryptocurrencies(currency.lowercase()))
    },
        label = {
            Text(
                text = currency,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        selected = (currency == stringResource(R.string.currency_usd_str) && viewModel.isUSD.value)
                || (currency == stringResource(R.string.currency_rub_str) && !viewModel.isUSD.value),
        colors = FilterChipDefaults.filterChipColors(
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        shape = CircleShape,
        border = null,
        modifier = Modifier.size(100.dp, 40.dp)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CryptList(
    viewModel: CryptViewModel,
    navigator: DestinationsNavigator,
    snackbarHostState: SnackbarHostState
) {
    val refreshScope = rememberCoroutineScope()
    val isRefreshing = remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        isRefreshing.value = true
        viewModel.createEvent(Event.LoadListOfCryptocurrencies)
    }

    val state = rememberPullRefreshState(isRefreshing.value, ::refresh)

    Box(
        Modifier.pullRefresh(state)
    ) {
        when (val result = viewModel.response.value) {
            is ApiState.SuccessLoadingList -> {
                isRefreshing.value = false
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(result.data) { cryptocurrency ->
                        CryptCard(viewModel, navigator, cryptocurrency)
                    }
                }
            }

            is ApiState.Loading -> {
                if (!isRefreshing.value) Loading()
            }

            is ApiState.Failure -> {
                if (!isRefreshing.value) Failure(viewModel)

                val msg = stringResource(R.string.error_short_str)
                LaunchedEffect(isRefreshing.value) {
                    if (isRefreshing.value) {
                        refreshScope.launch {
                            snackbarHostState.showSnackbar(msg)
                        }
                        isRefreshing.value = false
                    }
                }
            }

            else -> {

            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing.value,
            state = state,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun CryptCard(
    viewModel: CryptViewModel,
    navigator: DestinationsNavigator,
    cryptocurrency: Cryptocurrency
) {
    Row(
        modifier = Modifier
            .height(70.dp)
            .padding(20.dp, 10.dp)
            .clickable {
                viewModel.createEvent(Event.LoadCryptCard(cryptocurrency.id))
                navigator.navigate(ExtraInformationScreenDestination)
            }
    ) {
        AsyncImage(
            modifier = Modifier.weight(1f),
            model = cryptocurrency.imageURL,
            contentDescription = cryptocurrency.name
        )

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(3f)
                .padding(10.dp, 0.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = cryptocurrency.name,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = cryptocurrency.symbol.uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
        ) {

            val decimalFormatter = DecimalFormatter()
            val currentPrice = String.format("%.2f", cryptocurrency.currentPrice)
            val symbolCurrency = if (viewModel.isUSD.value) dollar.toString()
            else '\u20BD'.toString()
            Text(
                text = "$symbolCurrency " + decimalFormatter.formatForVisual(currentPrice),
                style = MaterialTheme.typography.bodyMedium,
            )

            var color = Color.Black
            var changePercentage = String.format("%.2f", cryptocurrency.marketCapChangePercentage)
            changePercentage = decimalFormatter.formatForVisual(changePercentage) + "%"
            when {
                (cryptocurrency.marketCapChangePercentage < 0) -> {
                    color = Color.Red
                }

                (cryptocurrency.marketCapChangePercentage > 0) -> {
                    changePercentage = "+$changePercentage"
                    color = colorResource(id = R.color.dark_green)
                }
            }
            Text(
                text = changePercentage,
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
        }
    }
}