package com.devmanishpatole.alzuraapplication.login.service

import com.devmanishpatole.alzuraapplication.login.model.LoginRequest
import com.devmanishpatole.alzuraapplication.login.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {

    @POST("operator/login")
    suspend fun login(
        @Header("Authorization") auth: String,
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}