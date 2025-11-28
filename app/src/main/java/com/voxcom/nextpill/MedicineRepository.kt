package com.voxcom.nextpill

class MedicineRepository(private val dao: MedicineDao) {

    val allMedicines = dao.getAllMedicines()

    suspend fun insert(medicine: MedicineInfo) {
        dao.insertMedicine(medicine)
    }
}
