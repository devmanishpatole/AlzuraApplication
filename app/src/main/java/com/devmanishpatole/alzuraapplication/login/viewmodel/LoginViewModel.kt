package com.devmanishpatole.alzuraapplication.login.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmanishpatole.alzuraapplication.base.BaseViewModel
import com.devmanishpatole.alzuraapplication.login.data.SessionManager
import com.devmanishpatole.alzuraapplication.login.model.LoginStatus
import com.devmanishpatole.alzuraapplication.login.repository.LoginRepository
import com.devmanishpatole.alzuraapplication.util.NetworkHelper
import com.devmanishpatole.alzuraapplication.util.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val repository: LoginRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _userLoggedInStatus = MutableLiveData<Boolean>()
    val userLoggedInStatus: LiveData<Boolean>
        get() = _userLoggedInStatus

    private val _loginResponse = MutableLiveData<Result<LoginStatus>>()
    val loginResponse: LiveData<Result<LoginStatus>>
        get() = _loginResponse

    private val _emailValidation = MutableLiveData<Boolean>()
    val emailValidation: LiveData<Boolean>
        get() = _emailValidation

    private val _passwordValidation = MutableLiveData<Boolean>()
    val passwordValidation: LiveData<Boolean>
        get() = _passwordValidation

    fun isUserLoggedIn() {
        viewModelScope.launch {
            repository.userData().collect {
                if (it.loggedIn) {
                    repository.userToken().collect { token ->
                        SessionManager.setToken(token.token)
                        _userLoggedInStatus.value = it.loggedIn
                    }
                } else {
                    _userLoggedInStatus.value = it.loggedIn
                }
            }
        }
    }

    fun login(username: String, password: String) {
        if (validateEmailAndPassword(username, password)) {
            performLogin(username, password)
        }
    }

    private fun performLogin(username: String, password: String) {
        if (networkHelper.isNetworkConnected()) {
            _loginResponse.value = Result.loading()
            viewModelScope.launch(exceptionHandler) {
                when (repository.login(username, password)) {
                    is LoginStatus.Success -> {
                        _loginResponse.postValue(Result.success())
                    }
                    is LoginStatus.Failure -> _loginResponse.postValue(Result.error())
                }
            }
        } else {
            _loginResponse.value = Result.noInternet()
        }
    }

    private fun validateEmailAndPassword(username: String, password: String): Boolean {
        var result = true

        if (username.isEmpty()) {
            _emailValidation.value = false
            result = false
        }
        if (password.isEmpty()) {
            _passwordValidation.value = false
            result = false
        }

        return result
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _loginResponse.postValue(Result.error(LoginStatus.Failure(0)))
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}