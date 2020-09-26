package com.devmanishpatole.alzuraapplication.orders.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.devmanishpatole.alzuraapplication.orders.model.OrderData
import com.devmanishpatole.alzuraapplication.orders.paging.OrdersSource
import com.devmanishpatole.alzuraapplication.orders.service.OrderService
import com.devmanishpatole.alzuraapplication.util.NetworkHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val service: OrderService,
    private val networkHelper: NetworkHelper
) {

    fun getOrders(descending: Boolean = true, range: String? = null): Flow<PagingData<OrderData>> {

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                OrdersSource(
                    service,
                    networkHelper,
                    descending,
                    range
                )
            }
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}