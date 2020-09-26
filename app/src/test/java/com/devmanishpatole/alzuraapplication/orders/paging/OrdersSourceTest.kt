package com.devmanishpatole.alzuraapplication.orders.paging

import com.devmanishpatole.alzuraapplication.base.BaseDataSource
import com.devmanishpatole.alzuraapplication.exception.NetworkException
import com.devmanishpatole.alzuraapplication.orders.model.OrderResponse
import com.devmanishpatole.alzuraapplication.orders.service.OrderService
import com.devmanishpatole.alzuraapplication.util.NetworkHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class OrdersSourceTest {

    private lateinit var orderSource: OrdersSource

    @MockK
    lateinit var service: OrderService

    @MockK
    lateinit var networkHelper: NetworkHelper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        orderSource = OrdersSource(service, networkHelper)
    }

    @Test
    fun loadFromNetwork_whenNoInternet_shouldThrowException() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns false
        try {
            orderSource.loadFromNetwork()
            fail()
        } catch (e: Exception) {
            assertTrue(e is NetworkException)
        }
    }

    @Test
    fun loadFromNetwork_whenResponseFails_shouldReturnResponse() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns true

        val response = mockk<Response<OrderResponse>>()
        every { response.isSuccessful } returns false

        coEvery { service.orders(BaseDataSource.THRESHOLD, 0, any(), any()) } returns response

        try {
            orderSource.loadFromNetwork()
            fail()
        } catch (e: Exception) {
            assertNotNull(e)
        }
    }

    @Test
    fun loadFromNetwork_shouldReturnResponse() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns true

        val response = mockk<Response<OrderResponse>>()
        every { response.isSuccessful } returns true
        every { response.body()?.data } returns listOf()

        coEvery { service.orders(BaseDataSource.THRESHOLD, 0, any(), any()) } returns response

        val data = orderSource.loadFromNetwork()
        assertNotNull(data)
        assertTrue(data?.isEmpty() == true)
    }

    @After
    fun tearDown() = clearAllMocks()
}