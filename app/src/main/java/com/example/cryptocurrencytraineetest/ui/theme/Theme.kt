package com.example.cryptocurrencytraineetest.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(

    primary = BrightOrange,
    onPrimary = Orange,
    primaryContainer = Orange,
    onPrimaryContainer = Color.White,
    secondary = Grey,
    onSecondary = Color.Black,
    secondaryContainer = Color.Black,
    onSecondaryContainer = Grey,
    tertiary = Color.Black,
    onTertiary = Grey,
    tertiaryContainer = Color.White,
    onTertiaryContainer = Color.Black,
    error = errorLight,
    onError = Color.White,
    errorContainer = Color.White,
    onErrorContainer = errorLight,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Grey,
    onSurfaceVariant = Color.Black,
    outline = Color.Black,
    outlineVariant = Color.Black,
    scrim = Color.Black,
    inverseSurface = Color.White,
    inverseOnSurface = Color.Black,
    inversePrimary = Grey,
    surfaceDim = Grey,
    surfaceBright = Grey,
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color.Black,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color.White,
)

@Composable
fun CryptocurrencyTraineeTestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}