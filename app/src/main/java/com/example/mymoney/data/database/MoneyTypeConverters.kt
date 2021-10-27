package com.example.mymoney.data.database

import androidx.room.TypeConverter
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.models.IncomeModel
import com.example.mymoney.models.MoneyModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MoneyTypeConverters {

    private var gson = Gson()

    @TypeConverter
    fun moneyToString(money: MoneyModel): String {
        return gson.toJson(money)
    }

    @TypeConverter
    fun moneyFromString(money: String): MoneyModel {
        val listType = object : TypeToken<MoneyModel>() {}.type
        return gson.fromJson(money, listType)
    }


}