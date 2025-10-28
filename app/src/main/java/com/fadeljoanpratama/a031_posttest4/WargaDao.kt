package com.fadeljoanpratama.a031_posttest4

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WargaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(warga: WargaEntity)

    @Query("SELECT * FROM warga ORDER BY id ASC")
    fun getAllWarga(): Flow<List<WargaEntity>>

    @Query("DELETE FROM warga")
    suspend fun deleteAll()
}