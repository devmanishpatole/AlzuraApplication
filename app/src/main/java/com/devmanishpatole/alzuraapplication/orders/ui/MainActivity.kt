package com.devmanishpatole.alzuraapplication.orders.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.devmanishpatole.alzuraapplication.R
import com.devmanishpatole.alzuraapplication.base.BaseActivity
import com.devmanishpatole.alzuraapplication.exception.NetworkException
import com.devmanishpatole.alzuraapplication.login.ui.LoginActivity
import com.devmanishpatole.alzuraapplication.login.viewmodel.LoginViewModel
import com.devmanishpatole.alzuraapplication.orders.adapter.OrderListAdapter
import com.devmanishpatole.alzuraapplication.orders.adapter.OrderLoadStateAdapter
import com.devmanishpatole.alzuraapplication.orders.viewmodel.OrderViewModel
import com.devmanishpatole.alzuraapplication.util.ViewUtil
import com.devmanishpatole.alzuraapplication.util.hide
import com.devmanishpatole.alzuraapplication.util.show
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<OrderViewModel>() {

    override val viewModel by viewModels<OrderViewModel>()

    private val loginViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var orderAdapter: OrderListAdapter

    override fun getLayoutId() = R.layout.activity_main

    private var calendar = Calendar.getInstance()
    private val date1: Date = Date(calendar.timeInMillis)
    private val date2: Date = Date(calendar.timeInMillis)
    private var range: String? = null
    private var checkedOrder = 0

    override fun setupView(savedInstanceState: Bundle?) {
        initialiseList()
        getOrders()
        initListeners()
    }

    private fun initListeners() {
        filter.setOnClickListener {
            showRangeDialog()
        }

        sort.setOnClickListener {
            showSortOrderDialog()
        }
    }

    private fun showRangeDialog() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setSelection(androidx.core.util.Pair(date1.time, date2.time))
        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())

        picker.addOnPositiveButtonClickListener {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            date1.time = it.first ?: 0
            date2.time = it.second ?: 0
            range = "${format.format(date1)},${format.format(date2)}"
            getOrders(checkedOrder == 0, range = range)
        }
    }

    private fun showSortOrderDialog() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        val group = arrayOf(getString(R.string.descending), getString(R.string.ascending))
        dialog.setTitle(getString(R.string.select_sort_order))
        dialog.setSingleChoiceItems(
            group, checkedOrder
        ) { dialog, item ->
            checkedOrder = item
            dialog.dismiss()
            getOrders(checkedOrder == 0, range = range)
        }
        val alert: AlertDialog = dialog.create()
        alert.show()
    }

    private fun initialiseList() {
        orderList.apply {
            orderAdapter.apply {
                adapter = this
                adapter = withLoadStateHeaderAndFooter(
                    header = OrderLoadStateAdapter { retry() },
                    footer = OrderLoadStateAdapter { retry() }
                )
            }
            setHasFixedSize(true)
        }

        orderAdapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                // Showing list when success
                is LoadState.NotLoading -> {
                    hideProgressbar()
                    if (orderAdapter.itemCount == 0) {
                        noOrders.show()
                    } else {
                        ViewUtil.hideView(noOrders, internetError)
                    }
                }
                // Showing progress for load
                is LoadState.Loading -> showProgressbar()
                // Showing no orders in case of error.
                is LoadState.Error -> {
                    hideProgressbar()
                    noOrders.show()
                    if ((loadState.source.refresh as LoadState.Error).error is NetworkException) {
                        internetError.show()
                    }

                }
            }

            // Popping error
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let { showMessage("\uD83D\uDE28 Error ${it.error}") }
        }
    }

    private fun getOrders(
        descending: Boolean = true,
        range: String? = null
    ) {
        lifecycleScope.launch {
            viewModel.getOrders(descending = descending, range = range).collectLatest {
                orderAdapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                loginViewModel.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}