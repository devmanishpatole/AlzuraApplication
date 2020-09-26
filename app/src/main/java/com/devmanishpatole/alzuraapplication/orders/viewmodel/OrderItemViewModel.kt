package com.devmanishpatole.alzuraapplication.orders.viewmodel

import com.devmanishpatole.alzuraapplication.base.BaseItemViewModel
import com.devmanishpatole.alzuraapplication.orders.model.OrderData
import javax.inject.Inject

// Required to handle operation related to each order.
class OrderItemViewModel @Inject constructor() : BaseItemViewModel<OrderData>()