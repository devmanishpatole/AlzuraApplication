package com.devmanishpatole.alzuraapplication.orders.model

data class Operator(
    val address: Address,
    val benefit: Double,
    val id: Int,
    val prices: List<Price>
)