package com.amv.simple.app.mysupernotes.presentation.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amv.simple.app.mysupernotes.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {
}