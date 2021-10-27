package com.example.mymoney.data

import androidx.lifecycle.LiveData
import com.example.mymoney.data.database.MoneyDao
import com.example.mymoney.data.database.entities.ExpenseEntity
import com.example.mymoney.data.database.entities.MoneyEntity
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

    suspend fun readExpenses(): List<ExpenseEntity> {
        return moneyDao.readExpenses()
    }

    suspend fun insertExpenseEntity(expenseEntity: ExpenseEntity) {
        moneyDao.insertExpenseEntity(expenseEntity)
    }
}