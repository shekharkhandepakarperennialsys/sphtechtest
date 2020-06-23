package com.sphtech.test.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sphtech.shared.core.exception.Failure
import com.sphtech.shared.core.result.Event
import timber.log.Timber

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 */
abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<Event<String>> = MutableLiveData()
    var sessionExpire: MutableLiveData<String> = MutableLiveData()
    var loading: MutableLiveData<Event<Boolean>> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        Timber.e(failure.toString())
        this.failure.value = Event(failure.javaClass.toString())
    }
}