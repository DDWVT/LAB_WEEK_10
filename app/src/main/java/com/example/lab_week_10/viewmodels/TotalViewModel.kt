package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab_week_10.database.TotalObject

class TotalViewModel: ViewModel() {
    private val _total = MutableLiveData<TotalObject>()
    val total: LiveData<TotalObject> = _total

    init {
        _total.postValue(TotalObject(value = 0, date = ""))
    }

    fun incrementTotal() {
        val currentTotal = _total.value ?: TotalObject(0, "")
        _total.postValue(currentTotal.copy(value = currentTotal.value + 1))
    }

    fun setTotal(newTotal: TotalObject) {
        _total.postValue(newTotal)
    }
}