package com.devmanishpatole.alzuraapplication.orders.paging

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.devmanishpatole.alzuraapplication.base.BaseDataSource
import com.devmanishpatole.alzuraapplication.exception.NetworkException
import com.devmanishpatole.alzuraapplication.orders.model.OrderData
import com.devmanishpatole.alzuraapplication.orders.service.OrderService
import com.devmanishpatole.alzuraapplication.util.NetworkHelper

class OrdersSource(
    private val service: OrderService,
    private val networkHelper: NetworkHelper,
    private val descendingOrder: Boolean = false,
    private val dateRange: String? = null
) : BaseDataSource<OrderData>() {

    override suspend fun loadFromLocalStorage(): List<OrderData>? {
        // In case we can load data from local storage.
        return null
    }

    override suspend fun loadFromNetwork(): List<OrderData>? {
        val results: List<OrderData>?

        if (networkHelper.isNetworkConnected()) {
            val order = if (descendingOrder) "-created_at" else "+created_at"
            val range = dateRange?.let { "btw; $dateRange" }
            val response =
                service.orders(
                    limit = THRESHOLD,
                    offset = position,
                    sort = order,
                    dateRange = range
                )

            if (response.isSuccessful) {
                results = response.body()?.data
            } else {
                throw Exception()
            }
        } else {
            throw NetworkException()
        }
        return results
    }

    override fun getPreviousKey() =
        if (position == STARTING_PAGE_INDEX) null else position - THRESHOLD

    override fun getNextKey(results: List<OrderData>) =
        if (results.isEmpty() || results.size < THRESHOLD) null else position + THRESHOLD

    companion object {
        const val ACTION_DATA_REFRESH = "ACTION_DATA_REFRESH"
    }
}