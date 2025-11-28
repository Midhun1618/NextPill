package com.voxcom.nextpill

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MedicineViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = MedicineDatabase.getDatabase(application).medicineDao()
    private val repository = MedicineRepository(dao)

    val medicines = repository.allMedicines

    fun addMedicine(medicine: MedicineInfo) {
        viewModelScope.launch {
            repository.insert(medicine)
        }
    }
}
