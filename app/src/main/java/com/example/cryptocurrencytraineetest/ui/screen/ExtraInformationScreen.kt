package com.example.cryptocurrencytraineetest.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cryptocurrencytraineetest.R
import com.example.cryptocurrencytraineetest.ui.screen.destinations.CryptListScreenDestination
import com.example.cryptocurrencytraineetest.ui.util.Failure
import com.example.cryptocurrencytraineetest.ui.util.Loading
import com.example.cryptocurrencytraineetest.data.ApiState
import com.example.cryptocurrencytraineetest.ui.viewmodel.CryptViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ExtraInformationScreen(
    navigator: DestinationsNavigator,
    viewModel: CryptViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 5.dp
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = viewModel.idCurrency.value.capitalize(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.navigate(CryptListScreenDestination) }) {
                            Image(
                                painter = painterResource(R.drawable.back_btn),
                                contentDescription = "back",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.padding(15.dp, 0.dp)
                )
            }
        },
        content = { innerPadding ->
            CardCurrencyWithExtraInformation(viewModel, innerPadding)
        }
    )
}

@Composable
fun CardCurrencyWithExtraInformation(
    viewModel: CryptViewModel,
    innerPadding: PaddingValues
) {
    when (val result = viewModel.response.value) {
        is ApiState.SuccessLoadingCoin -> {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp, 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = result.data.listOfImages["large"],
                    contentDescription = result.data.name,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(160.dp)
                )
                Text(
                    text = stringResource(R.string.description_str),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(0.dp, 10.dp)
                )
                result.data.description["en"]?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.caregories_str),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(0.dp, 10.dp)
                )
                Text(
                    text = result.data.categories.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        is ApiState.Loading -> {
            Loading()
        }

        is ApiState.Failure -> {
            Failure(viewModel)
        }

        else -> {}
    }
}