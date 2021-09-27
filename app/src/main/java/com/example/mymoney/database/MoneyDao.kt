package com.example.mymoney.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mymoney.database.entities.ExpenseEntity
import com.example.mymoney.database.entities.MoneyEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface MoneyDao {
    @Update
    suspend fun updateMoneyEntity(moneyEntity: MoneyEntity)

    @Insert
    suspend fun insertMoneyEntity(moneyEntity: MoneyEntity)

    @Query("SELECT * FROM money_table ORDER BY id ASC")
    fun readMoneyEntities(): LiveData<List<MoneyEntity>>

    @Query("SELECT * from money_table WHERE id = :id")
    fun getMoneyEntity(id: Int): LiveData<MoneyEntity>


    @Query("SELECT * FROM money_table ORDER BY id ASC")
    fun readExpenses(): LiveData<List<ExpenseEntity>>

    @Insert
    suspend fun insertExpenseEntity(expenseEntity: ExpenseEntity)
}