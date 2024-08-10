package com.example.cryptocurrencytraineetest.model

import com.google.gson.annotations.SerializedName

data class Cryptocurrency (
    @SerializedName("id")
    val id : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("image")
    val imageURL : String,
    @SerializedName("symbol")
    val symbol : String,
    @SerializedName("current_price")
    val currentPrice : Double,
    @SerializedName("ath_change_percentage")
    val changePercentage : Double
)