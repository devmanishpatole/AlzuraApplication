package com.devmanishpatole.alzuraapplication.orders.service

import com.devmanishpatole.alzuraapplication.orders.model.OrderResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OrderService {

    @GET("operator/orders")
    suspend fun orders(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("sort") sort: String,
        @Query("filter[created_at]") dateRange: String?
    ): Response<OrderResponse>

}