package com.devmanishpatole.alzuraapplication.login.repository

import android.util.Base64
import com.devmanishpatole.alzuraapplication.login.data.LoginDataStore
import com.devmanishpatole.alzuraapplication.login.data.SessionManager
import com.devmanishpatole.alzuraapplication.login.model.LoginRequest
import com.devmanishpatole.alzuraapplication.login.model.LoginStatus
import com.devmanishpatole.alzuraapplication.login.service.LoginService
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val dataStore: LoginDataStore,
    private val service: LoginService
) {
    fun userData() = dataStore.userDataFlow

    fun userToken() = dataStore.tokenFlow

    suspend fun login(username: String, password: String): LoginStatus {
        val status: LoginStatus

        val basicAuthPayload =
            "Basic " + Base64.encodeToString(
                "$username:$password".toByteArray(),
                Base64.NO_WRAP
            )
        val response =
            service.login(basicAuthPayload, LoginRequest(username, password))

        status = if (response.isSuccessful) {
            response.body()?.loginData?.token?.let { token ->
                SessionManager.setToken(token)
                dataStore.updateToken(token)
            }
            dataStore.updateUserStatus(loggedIn = true)
            LoginStatus.Success(response.body())
        } else {
            LoginStatus.Failure(response.code())
        }

        return status
    }

    suspend fun logout() {
        SessionManager.resetToken()
        dataStore.updateToken("")
        dataStore.updateUserStatus(loggedIn = false)
    }
}