package com.example.cryptocurrencytraineetest.model

import com.google.gson.annotations.SerializedName

data class CoinDescription(
    @SerializedName("name")
    val name: String,
    @SerializedName("categories")
    val categories : List<String>,
    @SerializedName("description")
    val description : Map<String, String>,
    @SerializedName("image")
    val listOfImages : Map<String, String>
)
