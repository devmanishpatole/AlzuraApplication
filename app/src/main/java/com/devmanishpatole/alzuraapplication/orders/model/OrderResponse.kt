package com.devmanishpatole.alzuraapplication.orders.model

data class OrderResponse(
    val data: List<OrderData>,
    val meta: Meta
)