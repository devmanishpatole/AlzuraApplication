package com.devmanishpatole.alzuraapplication.orders.adapter

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import com.devmanishpatole.alzuraapplication.base.BasePagingAdapter
import com.devmanishpatole.alzuraapplication.orders.model.OrderData
import com.devmanishpatole.alzuraapplication.orders.viewholder.OrderViewHolder

/**
 * Adapter for order list.
 */
class OrderListAdapter(parentLifecycle: Lifecycle) :
    BasePagingAdapter<OrderData, OrderViewHolder>(parentLifecycle, ORDER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderViewHolder(parent)

    companion object {
        private val ORDER_COMPARATOR = object : DiffUtil.ItemCallback<OrderData>() {
            override fun areItemsTheSame(
                oldItem: OrderData,
                newItem: OrderData
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: OrderData,
                newItem: OrderData
            ) = oldItem == newItem
        }
    }

}