package com.devmanishpatole.alzuraapplication.orders.model

data class OrderData(
    val created_at: String,
    val currency: Currency,
    val id: Int,
    val operator: Operator,
    val payment: Payment,
    val state: Int,
    val sum_original_price: Double,
    val updated_at: String
)