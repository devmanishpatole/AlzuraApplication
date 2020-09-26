package com.devmanishpatole.alzuraapplication.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.devmanishpatole.alzuraapplication.orders.ui.MainActivity
import com.devmanishpatole.alzuraapplication.R
import com.devmanishpatole.alzuraapplication.base.BaseActivity
import com.devmanishpatole.alzuraapplication.login.ui.LoginActivity
import com.devmanishpatole.alzuraapplication.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    companion object {
        private const val TIME_OUT = 1000L
    }

    override fun getLayoutId() = R.layout.activity_splash

    override fun setupView(savedInstanceState: Bundle?) {
        initObservers()
        Handler(Looper.getMainLooper()).postDelayed({
//            viewModel.setUserStatus(false)
            viewModel.isUserLoggedIn()
        }, TIME_OUT)
    }

    private fun initObservers() {
        viewModel.userLoggedInStatus.observe(this, { loggedIn ->
            startActivity(
                Intent(
                    this,
                    if (loggedIn) MainActivity::class.java else LoginActivity::class.java
                )
            )
            finish()
        })
    }
}