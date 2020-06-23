package com.sphtech.shared.database

import android.content.Context
import com.sphtech.shared.entities.RecordsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SPHTechRoomRepository  (application: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    var SPHTechDao: SPHTechDao?

    init {
        val db = SPHTechDatabase.getDatabase(application)
        SPHTechDao = db?.sphTechDao()
    }

    fun saveList(recordsDataList: List<RecordsData>) {
        launch  { saveListBG(recordsDataList) }
    }

    private suspend fun saveListBG(recordsDataList: List<RecordsData>){
        withContext(Dispatchers.IO){
            SPHTechDao?.saveList(recordsDataList)
        }
    }

    fun getList() = SPHTechDao?.getList()
}
