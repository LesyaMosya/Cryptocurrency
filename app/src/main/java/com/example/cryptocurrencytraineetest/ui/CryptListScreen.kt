package com.example.cryptocurrencytraineetest.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cryptocurrencytraineetest.R
import com.example.cryptocurrencytraineetest.model.Cryptocurrency
import com.example.cryptocurrencytraineetest.viewmodel.ApiState
import com.example.cryptocurrencytraineetest.viewmodel.CryptViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.example.cryptocurrencytraineetest.ui.destinations.ExtraInformationScreenDestination
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun CryptListScreen(
    navigator: DestinationsNavigator,
    viewModel: CryptViewModel = hiltViewModel()
) {

    val isUSD = remember { mutableStateOf(true) }
    Scaffold (
        topBar = {
            MediumTopAppBar(
                title = {
                    Column(
                        modifier = Modifier.padding(0.dp, 20.dp)
                    ) {
                        Text( stringResource(id = R.string.crypt_list_str))
                        Row {
                            CurrencyChip(viewModel, isUSD, stringResource(id = R.string.currency_usd_str))
                            Spacer(modifier = Modifier.width(20.dp))
                            CurrencyChip(viewModel, isUSD, stringResource(id = R.string.currency_rub_str))
                        }
                    }

                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.secondary
                ),
                windowInsets = TopAppBarDefaults.windowInsets
            )
        },
        content = { innerPadding ->
            CryptList(viewModel, navigator, innerPadding)

        }
    )
}

@Composable
fun CurrencyChip(
    viewModel: CryptViewModel,
    isUSD : MutableState<Boolean>,
    currency : String
){
    FilterChip(
        onClick = {
            isUSD.value = !isUSD.value
            viewModel.getCryptocurrencies(currency.lowercase())
        },
        label = {
            Text(currency)
        },
        selected = (currency == stringResource(R.string.currency_usd_str) && isUSD.value) ||
                (currency == stringResource(R.string.currency_rub_str) && !isUSD.value),
        colors = FilterChipDefaults.filterChipColors(
            selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
            selectedContainerColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = CircleShape,
        modifier = Modifier.size(120.dp, 50.dp)
    )
}

@Composable
fun CryptList(
    viewModel: CryptViewModel,
    navigator: DestinationsNavigator,
    innerPadding: PaddingValues
) {

    when (val result = viewModel.response.value) {
        is ApiState.SuccessLoadingList -> {
            LazyColumn (
                modifier = Modifier.padding(innerPadding)
            ) {
                items(result.data) { cryptocurrency ->
                    CryptCard(viewModel, cryptocurrency, navigator)
                }
            }
        }
        is ApiState.Loading -> {
            Loading()
        }
        is ApiState.Failure -> {
            Failure()
        }
        else -> {

        }
    }
}

@Composable
fun CryptCard(
    viewModel: CryptViewModel,
    cryptocurrency: Cryptocurrency,
    navigator: DestinationsNavigator
) {
    Row (
        modifier = Modifier
            .height(70.dp)
            .padding(20.dp, 10.dp)
            .clickable {
                viewModel.getCryptocurrencyByName(cryptocurrency.id)
                navigator.navigate(ExtraInformationScreenDestination)
            }
    ) {
        AsyncImage(
            modifier = Modifier.weight(1f),
            model = cryptocurrency.imageURL,
            contentDescription = cryptocurrency.name
        )
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(3f)
                .padding(10.dp, 0.dp)
                .fillMaxHeight()
        ) {
            Text(cryptocurrency.name)
            Text(
                cryptocurrency.symbol.uppercase(),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Column (
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
        ) {
            val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
            /*            val currentPrice = df.format(cryptocurrency.currentPrice.toBigDecimal())
                        val changePercentage = df.format(cryptocurrency.changePercentage.toBigDecimal())+"%"*/
            val currentPrice = String.format("%.2f", cryptocurrency.currentPrice)
            val changePercentage = String.format("%.2f", cryptocurrency.changePercentage)+"%"
            Text(currentPrice)
            Text(
                text = when {
                    (cryptocurrency.changePercentage > 0) -> "+$changePercentage"
                    else -> changePercentage
                },
                color = when {
                    (cryptocurrency.changePercentage < 0) -> Color.Red
                    (cryptocurrency.changePercentage > 0) -> Color.Green
                    else -> MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}