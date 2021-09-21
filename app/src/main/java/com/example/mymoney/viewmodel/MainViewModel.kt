package com.example.mymoney.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymoney.database.DataStoreRepository
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    private lateinit var moneyEntity: MoneyEntity

    val readMoney: LiveData<List<MoneyEntity>> = repository.local.readMoney()
    val readEntity: LiveData<MoneyEntity> = repository.local.getMoneyEntity(1)


    fun insertMoneyEntity(moneyEntity: MoneyEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertMoneyEntity(moneyEntity)
        }
    }

    fun updateMoneyEntity(moneyEntity: MoneyEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.updateMoneyEntity(moneyEntity)
        }
    }





}