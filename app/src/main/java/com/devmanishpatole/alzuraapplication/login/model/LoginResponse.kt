package com.devmanishpatole.alzuraapplication.login.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data") val loginData: LoginData
)