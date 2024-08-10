package com.example.cryptocurrencytraineetest.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cryptocurrencytraineetest.R


@Composable
fun Loading() {
    Box (
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
        val angle by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing)
            ), label = "angle"
        )
        Image (
            painter = painterResource(R.drawable.linear),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = angle
                }
                .size(50.dp)
        )
    }
}

@Composable
fun Failure() {
    Column {
        Image(
            painter = painterResource(R.drawable.error),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.error_str)
        )
        Button(onClick = { }) {

        }
    }
}