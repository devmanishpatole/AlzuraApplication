package com.devmanishpatole.alzuraapplication.login.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("Username") val userName: String,
    @SerializedName("Password") val password: String
)