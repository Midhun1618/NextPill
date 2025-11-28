package com.voxcom.nextpill

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {

    @Insert
    suspend fun insertMedicine(medicine: MedicineInfo)

    @Query("SELECT * FROM medicine_table ORDER BY id DESC")
    fun getAllMedicines(): Flow<List<MedicineInfo>>
}
