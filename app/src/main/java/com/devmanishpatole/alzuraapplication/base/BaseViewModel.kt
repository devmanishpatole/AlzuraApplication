package com.devmanishpatole.alzuraapplication.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmanishpatole.alzuraapplication.util.Result


abstract class BaseViewModel : ViewModel() {

    private val _messageStringId: MutableLiveData<Result<Int>> = MutableLiveData()
    val messageStringId: LiveData<Result<Int>>
        get() = _messageStringId

    private val _messageString: MutableLiveData<Result<String>> = MutableLiveData()
    val messageString: LiveData<Result<String>>
        get() = _messageString

}