package com.example.mymoney.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class KeyboardViewModel(application: Application): AndroidViewModel(application) {

    private var _amount: MutableLiveData<String> = MutableLiveData("0")
    val amount: LiveData<String>
        get() = _amount

    fun addNumb(number: String) {
        if(_amount.value == "0") {
            _amount.value = ""
        }
        _amount.value = _amount.value + number
    }

    fun deleteNumb() {
        if (_amount.value?.length!! <= 1) {
            _amount.value = "0"
        }
        else {
            _amount.value = _amount.value!!.substring(0, _amount.value!!.length - 1)
        }
    }
}