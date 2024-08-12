package com.example.cryptocurrencytraineetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cryptocurrencytraineetest.ui.theme.CryptocurrencyTraineeTestTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import com.example.cryptocurrencytraineetest.ui.screen.NavGraphs

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptocurrencyTraineeTestTheme {
                    DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

