package com.fadeljoanpratama.a031_posttest4

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [WargaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseWarga : RoomDatabase() {
    abstract fun wargaDao(): WargaDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseWarga? = null

        fun getDatabase(context: Context): DatabaseWarga {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseWarga::class.java,
                    "warga_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}