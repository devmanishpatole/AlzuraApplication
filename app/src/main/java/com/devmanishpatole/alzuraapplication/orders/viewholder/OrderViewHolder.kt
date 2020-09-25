package com.devmanishpatole.alzuraapplication.orders.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleRegistry
import com.devmanishpatole.alzuraapplication.R
import com.devmanishpatole.alzuraapplication.base.BaseItemViewHolder
import com.devmanishpatole.alzuraapplication.orders.model.OrderData
import com.devmanishpatole.alzuraapplication.orders.viewmodel.OrderItemViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import kotlinx.android.synthetic.main.row_order.view.*

class OrderViewHolder(parent: ViewGroup) :
    BaseItemViewHolder<OrderData, OrderItemViewModel>(R.layout.row_order, parent) {

    override lateinit var lifecycleRegistry: LifecycleRegistry

    override lateinit var viewModel: OrderItemViewModel

    override fun setupView(view: View) {
        //No Implementation
    }

    @SuppressLint("SetTextI18n")
    override fun bind(data: OrderData) {
        with(itemView) {
            orderId.text = data.id.toString()
            paymentMethod.text = data.payment.name
            createdDateTime.text = data.created_at
            price.text = "€ ${data.sum_original_price}"
        }
    }

    @InstallIn(ActivityComponent::class)
    @EntryPoint
    interface ProviderThumbnailViewModel {
        fun orderItemViewModel(): OrderItemViewModel
    }

    private fun getViewModel(activity: AppCompatActivity): OrderItemViewModel {
        val hiltEntryPoint = EntryPointAccessors.fromActivity(
            activity,
            ProviderThumbnailViewModel::class.java
        )
        return hiltEntryPoint.orderItemViewModel()
    }

    override fun injectDependency() {
        lifecycleRegistry = LifecycleRegistry(this)
        viewModel = getViewModel(itemView.context as AppCompatActivity)
    }

}