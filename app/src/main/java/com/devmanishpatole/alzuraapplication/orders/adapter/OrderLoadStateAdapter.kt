package com.devmanishpatole.alzuraapplication.orders.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.devmanishpatole.alzuraapplication.orders.viewholder.OrderLoadStateViewHolder

/**
 * Adapter to handle lazy loading buffering and error.
 */
class OrderLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<OrderLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: OrderLoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = OrderLoadStateViewHolder.create(parent, retry)
}