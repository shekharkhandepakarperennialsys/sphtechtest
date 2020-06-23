package com.sphtech.shared.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sphtech.shared.entities.RecordsData
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

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

        val ROOM_ENCRYPTION_PASSPHRASE = "SPHTechApp"
        const val DATABASE_NAME = "sph_tech_database"


        fun getDatabase(context: Context): SPHTechDatabase? {

//            val passphrase: ByteArray =
//                SQLiteDatabase.getBytes(ROOM_ENCRYPTION_PASSPHRASE.toCharArray())
//            val factory = SupportFactory(passphrase)
//            val state: SQLCipherUtils.State = SQLCipherUtils.getDatabaseState(
//                context,
//                DATABASE_NAME
//            )

            /*if (state == SQLCipherUtils.State.UNENCRYPTED) {
                try {
                    SQLCipherUtils.encrypt(
                        context,
                        DATABASE_NAME,
                        passphrase
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }*/
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    SPHTechDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
//                    .openHelperFactory(factory)
                    .build()
            }
            return INSTANCE
        }
    }
}
