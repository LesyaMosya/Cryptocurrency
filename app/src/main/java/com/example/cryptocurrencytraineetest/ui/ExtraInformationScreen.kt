package com.example.cryptocurrencytraineetest.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cryptocurrencytraineetest.R
import com.example.cryptocurrencytraineetest.ui.destinations.CryptListScreenDestination
import com.example.cryptocurrencytraineetest.viewmodel.ApiState
import com.example.cryptocurrencytraineetest.viewmodel.CryptViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ExtraInformationScreen(
    navigator: DestinationsNavigator,
    viewModel: CryptViewModel = hiltViewModel()
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Валюта") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigate(CryptListScreenDestination)}) {
                        Image(
                            painter = painterResource(R.drawable.back_btn),
                            contentDescription = "back",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            CardCurrency(viewModel, innerPadding)
        }
    )
}

@Composable
fun CardCurrency(
    viewModel: CryptViewModel,
    innerPadding: PaddingValues
) {
    when (val result = viewModel.response.value) {
        is ApiState.SuccessLoadingCoin -> {
            Column (
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp, 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = result.data.listOfImages["thumb"],
                    contentDescription = result.data.name,
                )
                Text(stringResource(R.string.description_str))
                result.data.description["en"]?.let {
                    Text(it)
                }
                Text(stringResource(R.string.caregories_str))
                Text(result.data.categories.toString())
            }
        }
        is ApiState.Loading -> {
            Loading()
        }
        is ApiState.Failure -> {
            Failure()
        }
        else -> {}
    }
}