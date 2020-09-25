package com.devmanishpatole.alzuraapplication.login.model

import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("expire_at") val expireAt: Int,
    val token: String
)