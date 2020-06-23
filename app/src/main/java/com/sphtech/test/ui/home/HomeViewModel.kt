package com.sphtech.test.ui.home

import android.content.Context
import com.sphtech.shared.network.repository.DataAmountRepository
import com.sphtech.test.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    context: Context,
    private val dataAmountRepository: DataAmountRepository
) : BaseViewModel() {

}

