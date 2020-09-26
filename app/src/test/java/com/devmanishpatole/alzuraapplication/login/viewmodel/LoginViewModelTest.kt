package com.devmanishpatole.alzuraapplication.login.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmanishpatole.alzuraapplication.CoroutinesTestRule
import com.devmanishpatole.alzuraapplication.login.model.LoginStatus
import com.devmanishpatole.alzuraapplication.login.repository.LoginRepository
import com.devmanishpatole.alzuraapplication.util.NetworkHelper
import com.devmanishpatole.alzuraapplication.util.Status
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @MockK
    lateinit var repository: LoginRepository

    @MockK
    lateinit var networkHelper: NetworkHelper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = LoginViewModel(repository, networkHelper)
    }

    @Test
    fun validateEmailAndPassword_withValidInputs() {
        viewModel.emailValidation.observeForever { valid ->
            assertNotNull(valid)
            assertTrue(valid)
        }
        viewModel.passwordValidation.observeForever { valid ->
            assertNotNull(valid)
            assertTrue(valid)
        }
        viewModel.validateEmailAndPassword("12345", "password")
    }

    @Test
    fun validateEmailAndPassword_withInvalidInputs() {
        viewModel.emailValidation.observeForever { valid ->
            assertNotNull(valid)
            assertFalse(valid)
        }
        viewModel.passwordValidation.observeForever { valid ->
            assertNotNull(valid)
            assertFalse(valid)
        }
        viewModel.validateEmailAndPassword("", "")
    }

    @Test
    fun performLogin_withNoInternet_shouldShowError() {
        every { networkHelper.isNetworkConnected() } returns false

        viewModel.loginResponse.observeForever { response ->
            assertNotNull(response)
            when (response.status) {
                Status.NO_INTERNET -> return@observeForever
                else -> fail()
            }
        }
        viewModel.login("12345", "password")

        verify { networkHelper.isNetworkConnected() }
        confirmVerified(networkHelper)
    }

    @Test
    fun performLogin_withValidInput_shouldShowSuccess() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            every { networkHelper.isNetworkConnected() } returns true
            coEvery { repository.login(any(), any()) } returns LoginStatus.Success(mockk())

            viewModel.loginResponse.observeForever { response ->
                assertNotNull(response)
                when (response.status) {
                    Status.LOADING -> assertNull(response.data)
                    Status.SUCCESS -> return@observeForever
                    else -> fail()
                }
            }
            viewModel.login("12345", "password")

            verify { networkHelper.isNetworkConnected() }
            coVerify { repository.login(any(), any()) }
            confirmVerified(networkHelper, repository)
        }

    @Test
    fun performLogin_whenRepositoryReturnsFailure_shouldShowSuccess() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            every { networkHelper.isNetworkConnected() } returns true
            coEvery { repository.login(any(), any()) } returns LoginStatus.Failure(0)

            viewModel.loginResponse.observeForever { response ->
                assertNotNull(response)
                when (response.status) {
                    Status.LOADING -> assertNull(response.data)
                    Status.ERROR -> return@observeForever
                    else -> fail()
                }
            }
            viewModel.login("12345", "password")

            verify { networkHelper.isNetworkConnected() }
            coVerify { repository.login(any(), any()) }
            confirmVerified(networkHelper, repository)
        }

    @After
    fun tearDown() = clearAllMocks()
}