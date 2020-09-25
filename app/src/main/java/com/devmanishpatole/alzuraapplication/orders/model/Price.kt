package com.devmanishpatole.alzuraapplication.orders.model

data class Price(
    val gross: Double,
    val net: Double,
    val tax: Tax
)