package com.devmanishpatole.alzuraapplication.orders.model

data class Tax(
    val rate: Int,
    val type: String,
    val value: Double
)