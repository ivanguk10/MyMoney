package com.example.mymoney

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MoneyDao {
    @Update
    suspend fun updateMoneyEntity(moneyEntity: MoneyEntity)

    @Insert
    suspend fun insertMoneyEntity(moneyEntity: MoneyEntity)

    @Query("SELECT * FROM money_table ORDER BY id ASC")
    fun readMoneyEntities(): List<MoneyEntity>
}