package com.example.mymoney.database

import androidx.lifecycle.LiveData
import com.example.mymoney.database.entities.MoneyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    var moneyDao: MoneyDao
) {
    fun readMoney(): LiveData<List<MoneyEntity>> {
        return moneyDao.readMoneyEntities()
    }

    suspend fun insertMoneyEntity(moneyEntity: MoneyEntity) {
        moneyDao.insertMoneyEntity(moneyEntity)
    }

    suspend fun updateMoneyEntity(moneyEntity: MoneyEntity) {
        moneyDao.updateMoneyEntity(moneyEntity)
    }

    fun getMoneyEntity(id: Int): LiveData<MoneyEntity> {
        return moneyDao.getMoneyEntity(id)
    }
}