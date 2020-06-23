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
import com.sphtech.test.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    context: Context,
    private val dataAmountRepository: DataAmountRepository
) : BaseViewModel() {

    private var sPHTechRoomRepository: SPHTechRoomRepository = SPHTechRoomRepository(context)

    private var resourceId = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"
    private var limit = 50

    var allList = arrayListOf<RecordsData>()

    private val _dataAmountAPIObserver = MutableLiveData<MutableMap<String, List<RecordsData>>>()
    val dataAmountAPIObserver: LiveData<MutableMap<String, List<RecordsData>>> =
        _dataAmountAPIObserver

    fun callDataAmountAPI() {
        loading.postValue(Event(true))
        getDBList().invokeOnCompletion {
            viewModelScope.launch(Dispatchers.IO) {
                when (val result = dataAmountRepository.callDataAmountAPI(resourceId, limit)) {
                    is Results.Success -> {
                        saveToDB(result.data.result.records)
                        allList.clear()
                        allList.addAll(result.data.result.records)

                        val byLength = allList.groupBy {
                            it.quarter.subSequence(0, 4).toString()
                        }
                        _dataAmountAPIObserver.postValue(byLength.toMutableMap())
                    }

                    is Results.Error -> {
                        failure.postValue(Event(result.exception.message.toString()))
                    }
                }
                loading.postValue(Event(false))
            }
        }
    }

    private fun saveToDB(recordsDataList: List<RecordsData>) {
        CoroutineScope(Dispatchers.IO).launch {
            sPHTechRoomRepository.saveList(recordsDataList)
        }
    }

    private fun getDBList() = viewModelScope.async {
        viewModelScope.launch(Dispatchers.IO) {
            sPHTechRoomRepository.getList()?.let {
                println("db success")
                allList.clear()
                allList.addAll(it)

                val byLength = allList.groupBy {
                    it.quarter.subSequence(0, 4).toString()
                }
                _dataAmountAPIObserver.postValue(byLength.toMutableMap())
            }
        }
    }
}
