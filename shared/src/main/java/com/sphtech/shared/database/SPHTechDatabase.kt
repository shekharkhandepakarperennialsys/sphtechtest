package com.sphtech.shared.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sphtech.shared.entities.RecordsData

@Database(
    entities = [RecordsData::class],
    version = 1,
    exportSchema = false
)

abstract class SPHTechDatabase : RoomDatabase() {

    abstract fun sphTechDao(): SPHTechDao

    companion object {

        @Volatile
        private var INSTANCE: SPHTechDatabase? = null
        const val DATABASE_NAME = "sph_tech_database"

        fun getDatabase(context: Context): SPHTechDatabase? {

            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    SPHTechDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }
    }
}
