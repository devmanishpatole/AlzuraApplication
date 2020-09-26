package com.devmanishpatole.alzuraapplication.orders.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.devmanishpatole.alzuraapplication.base.BaseViewModel
import com.devmanishpatole.alzuraapplication.orders.repository.OrderRepository

class OrderViewModel @ViewModelInject constructor(
    private val repository: OrderRepository
) : BaseViewModel() {

    fun getOrders(descending: Boolean = true, range: String? = null) =
        repository.getOrders(descending, range).cachedIn(viewModelScope)

}