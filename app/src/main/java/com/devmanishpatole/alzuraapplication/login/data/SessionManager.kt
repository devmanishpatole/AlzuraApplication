package com.devmanishpatole.alzuraapplication.login.data

object SessionManager {
    private var token = ""

    fun getToken() = token

    fun setToken(token: String) {
        this.token = token
    }

    fun resetToken() {
        token = ""
    }
}