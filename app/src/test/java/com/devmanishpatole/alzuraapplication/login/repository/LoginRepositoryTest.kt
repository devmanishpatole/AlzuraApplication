package com.devmanishpatole.alzuraapplication.login.repository

import com.devmanishpatole.alzuraapplication.login.data.LoginDataStore
import com.devmanishpatole.alzuraapplication.login.data.SessionManager
import com.devmanishpatole.alzuraapplication.login.model.LoginResponse
import com.devmanishpatole.alzuraapplication.login.model.LoginStatus
import com.devmanishpatole.alzuraapplication.login.service.LoginService
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class LoginRepositoryTest {

    private lateinit var repository: LoginRepository

    @MockK
    lateinit var dataStore: LoginDataStore

    @MockK
    lateinit var service: LoginService

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = LoginRepository(dataStore, service)
    }

    @Test
    fun test_whenServiceFails_shouldReturnError() = runBlocking {
        val response = mockk<Response<LoginResponse>>()
        every { response.isSuccessful } returns false
        every { response.code() } returns 500

        coEvery { service.login(any(), any()) } returns response

        val status = repository.login("12345", "password")

        assertTrue(status is LoginStatus.Failure)
        if (status is LoginStatus.Failure) {
            assertEquals(500, status.error)
        } else {
            fail()
        }

        coVerify { service.login(any(), any()) }
        confirmVerified(service)
    }

    @Test
    fun test_whenServiceSuccess_shouldReturnLoginResponse() = runBlocking {
        val response = mockk<Response<LoginResponse>>()
        every { response.isSuccessful } returns true

        every { response.body() } returns mockk()
        every { response.body()?.loginData?.token } returns "token"

        coEvery { service.login(any(), any()) } returns response

        coEvery { dataStore.updateToken(any()) } just Runs
        coEvery { dataStore.updateUserStatus(loggedIn = true) } just Runs

        val status = repository.login("12345", "password")

        assertTrue(status is LoginStatus.Success)
        if (status is LoginStatus.Success) {
            assertNotNull(status.success)
        }
        assertTrue(SessionManager.getToken().isNotEmpty())
        assertEquals("token", SessionManager.getToken())

        coVerify { service.login(any(), any()) }
        coVerify { dataStore.updateToken(any()) }
        coVerify { dataStore.updateUserStatus(loggedIn = true) }
        confirmVerified(service, dataStore)
    }

    @Test
    fun test_logout() = runBlocking {
        SessionManager.setToken("12345")

        coEvery { dataStore.updateToken(any()) } just Runs
        coEvery { dataStore.updateUserStatus(loggedIn = false) } just Runs

        repository.logout()

        assertTrue(SessionManager.getToken().isEmpty())

        coVerify { dataStore.updateToken(any()) }
        coVerify { dataStore.updateUserStatus(loggedIn = false) }
        confirmVerified(dataStore)
    }

}