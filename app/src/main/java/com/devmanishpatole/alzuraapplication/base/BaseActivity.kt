package com.devmanishpatole.alzuraapplication.base

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.devmanishpatole.alzuraapplication.R
import com.devmanishpatole.alzuraapplication.util.ViewUtil.hideView
import com.devmanishpatole.alzuraapplication.util.ViewUtil.showView
import kotlinx.android.synthetic.main.base_layout.*

/**
 * Base Activity for all activity.
 *
 * Handles common operations.
 * Observes message live data from base view model to display toast messages.
 *
 */
abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    abstract val viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        val content = layoutInflater.inflate(getLayoutId(), null)
        mainViewContainer.addView(content)
        initObserver()
        setupView(savedInstanceState)
    }

    /**
     * Observes the message changes from base view model.
     */
    private fun initObserver() {
        viewModel.messageString.observe(this, {
            it.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(this, {
            it.data?.run { showMessage(this) }
        })
    }

    /**
     * Shows the fill screen progress.
     */
    protected fun showProgressbar() {
        hideView(mainViewContainer, progressText, noInternet)
        showView(progressbar)
    }

    /**
     * Hides the progress.
     */
    protected fun hideProgressbar() {
        showView(mainViewContainer)
        hideView(noInternet, progressText, progressbar)
    }

    /**
     * Shows the fill screen progress.
     *
     * @param text - display over progress
     */
    protected fun showProgressbarWithText(text: String) {
        progressText.text = text
        showView(progressText, progressbar)
        hideView(mainViewContainer, noInternet)
    }

    protected fun showNoInternetError() {
        showView(noInternet, errorText)
        hideView(mainViewContainer, progressText, progressbar)
    }

    fun hideKeyboard() {
        val inputMethodManager: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun showMessage(message: String) =
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun setupView(savedInstanceState: Bundle?)

}