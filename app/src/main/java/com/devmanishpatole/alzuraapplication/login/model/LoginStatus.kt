package com.devmanishpatole.alzuraapplication.login.model

import com.devmanishpatole.alzuraapplication.R

sealed class LoginStatus {
    /**
     * Returns the data
     */
    class Success(val success: LoginResponse?) : LoginStatus()

    /**
     * Returns the error code
     */
    class Failure(val error: Int, var errorMessageId: Int = R.string.something_went_wrong) :
        LoginStatus()
}