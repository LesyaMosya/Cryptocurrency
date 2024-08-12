package com.example.cryptocurrencytraineetest.ui.util

import android.icu.text.DecimalFormatSymbols

class DecimalFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {

    private val decimalSeparator = symbols.decimalSeparator

    fun formatForVisual(input: String): String {

        val split = input.split(decimalSeparator)

        val intPart = split[0]
            .reversed()
            .chunked(3)
            .joinToString(separator = ",")
            .reversed()

        val fractionPart = split.getOrNull(1)

        return if (fractionPart == null) "$intPart.00" else "$intPart.$fractionPart"
    }
}