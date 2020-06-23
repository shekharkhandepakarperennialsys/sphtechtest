package com.sphtech.shared.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sphtech.shared.entities.RecordsData

@Dao
interface SPHTechDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(userCardsList: List<RecordsData>): Array<Long>

    @Query("SELECT * from records_data")
    fun getList(): List<RecordsData>
}