package com.example.mymoney

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [MoneyEntity::class],
    version = 1,
    exportSchema = true
)
//нужен конвертер
abstract class MoneyDatabase: RoomDatabase() {
    abstract fun moneyDao(): MoneyDao
}