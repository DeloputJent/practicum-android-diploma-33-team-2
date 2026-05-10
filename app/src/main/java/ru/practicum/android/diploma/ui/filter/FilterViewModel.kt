package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    var isCheckedOnlyWithSalary = false
    val isOnlyWithSalaryLiveData = MutableLiveData<Boolean>(
        isCheckedOnlyWithSalary
    )
    fun observeOnlyWithSalaryState(): LiveData<Boolean> = isOnlyWithSalaryLiveData
}
