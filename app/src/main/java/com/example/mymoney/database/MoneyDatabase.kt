package com.example.mymoney.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mymoney.database.entities.MoneyEntity


@Database(
    entities = [MoneyEntity::class],
    version = 1,
    exportSchema = true
)
//нужен конвертер
@TypeConverters(MoneyTypeConverters::class)
abstract class MoneyDatabase: RoomDatabase() {
    abstract fun moneyDao(): MoneyDao
}