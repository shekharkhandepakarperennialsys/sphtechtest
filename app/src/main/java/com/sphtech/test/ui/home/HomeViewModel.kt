package com.sphtech.test.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sphtech.shared.core.result.Event
import com.sphtech.shared.core.result.Results
import com.sphtech.shared.database.SPHTechRoomRepository
import com.sphtech.shared.entities.RecordsData
import com.sphtech.shared.network.repository.DataAmountRepository
import com.sphtech.shared.network.repository.prefs.SharedPreferenceStorage
import com.sphtech.test.base.BaseViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    context: Context,
    private val dataAmountRepository: DataAmountRepository
) : BaseViewModel() {

}

