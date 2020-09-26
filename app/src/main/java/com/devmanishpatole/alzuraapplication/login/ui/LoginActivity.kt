package com.devmanishpatole.alzuraapplication.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.devmanishpatole.alzuraapplication.orders.ui.MainActivity
import com.devmanishpatole.alzuraapplication.R
import com.devmanishpatole.alzuraapplication.base.BaseActivity
import com.devmanishpatole.alzuraapplication.login.viewmodel.LoginViewModel
import com.devmanishpatole.alzuraapplication.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun getLayoutId() = R.layout.activity_login

    override fun setupView(savedInstanceState: Bundle?) {
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        loginButton.setOnClickListener {
            viewModel.login(editUsername.text.toString(), editPassword.text.toString())
        }
    }

    private fun initObservers() {
        editUsername.addTextChangedListener {
            usernameWrapper.error = null
        }
        editPassword.addTextChangedListener {
            passwordWrapper.error = null
        }

        viewModel.emailValidation.observe(this, { valid ->
            if (!valid) {
                usernameWrapper.error = getString(R.string.enter_valid_email)
            } else {
                usernameWrapper.error = null
            }
        })

        viewModel.passwordValidation.observe(this, { valid ->
            if (!valid) {
                passwordWrapper.error = getString(R.string.enter_valid_password)
            } else {
                passwordWrapper.error = null
            }
        })

        viewModel.loginResponse.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                Status.LOADING -> showProgressbarWithText(getString(R.string.loading))
                Status.NO_INTERNET -> showMessage(getString(R.string.no_internet_connection))
                else -> {
                    hideProgressbar()
                    showMessage(getString(R.string.something_went_wrong))
                }
            }
        })

    }
}