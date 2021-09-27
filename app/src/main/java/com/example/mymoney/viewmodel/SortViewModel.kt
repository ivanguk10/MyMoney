package com.example.mymoney.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoney.database.DataStoreRepository
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.models.ExpenseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class SortViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    application: Application
): AndroidViewModel(application) {

    val readSortCardType = dataStoreRepository.readSortType

    fun saveSortCardType(cardId: Int, sortId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveSortType(
                cardId,
                sortId
            )
        }
    }

    fun sortWeek(money: MoneyEntity?): ArrayList<ExpenseModel> {

        val newList: ArrayList<ExpenseModel> = arrayListOf()

        money?.money?.expenses?.forEach {
            val date = stringToLocaleDate(it.date)
            val dayOfWeek = WeekFields.of(Locale.getDefault()).dayOfWeek()
            val start = LocalDate.now().with(dayOfWeek, 1)
            val end = start.plusDays(6)
            if (date == start || date == end || date.isAfter(start) && date.isBefore(end)) {
                newList.add(it)
            }
        }
        return newList
    }

    fun sortMonth(money: MoneyEntity?): ArrayList<ExpenseModel> {
        val newList: ArrayList<ExpenseModel> = arrayListOf()
        val now = LocalDate.now()
        val start = now.withDayOfMonth(1)
        val end = now.withDayOfMonth(now.lengthOfMonth())

        money?.money?.expenses?.forEach {
            val date = stringToLocaleDate(it.date)
            if (date == start || date == end || date.isAfter(start) && date.isBefore(end)) {
                newList.add(it)
            }
        }
        return newList
    }

    fun sortToday(money: MoneyEntity?): ArrayList<ExpenseModel> {
        val newList: ArrayList<ExpenseModel> = arrayListOf()
        val today = LocalDate.now()

        money?.money?.expenses?.forEach {
            val date = stringToLocaleDate(it.date)
            if (date == today) {
                newList.add(it)
            }
        }
        return newList
    }

    fun sortYesterday(money: MoneyEntity?): ArrayList<ExpenseModel> {
        val newList: ArrayList<ExpenseModel> = arrayListOf()
        val yesterday = LocalDate.now().minusDays(1)

        money?.money?.expenses?.forEach {
            val date = stringToLocaleDate(it.date)
            if (date == yesterday) {
                newList.add(it)
            }
        }
        return newList
    }

    fun sortYear(money: MoneyEntity?): ArrayList<ExpenseModel> {
        val newList: ArrayList<ExpenseModel> = arrayListOf()
        val now = LocalDate.now()
        val start = now.withDayOfYear(1)
        val end = now.withDayOfYear(now.lengthOfYear())

        money?.money?.expenses?.forEach {
            val date = stringToLocaleDate(it.date)
            if (date == start || date == end || date.isAfter(start) && date.isBefore(end)) {
                newList.add(it)
            }
        }
        return newList
    }

    private fun stringToLocaleDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return LocalDate.parse(dateString.dropLast(6), formatter)
    }

    fun sortByTypeAndAmount(expenses: ArrayList<ExpenseModel>): Map<String, Float> {
        val hashMap = HashMap<String, Float>()

        expenses.forEach { expense ->
            if (hashMap.containsKey(expense.type)) {
                //hashMap.compute(expense.type) {key: String, value: Float? -> value!! + expense.amount }
                val amount = hashMap[expense.type]
                hashMap[expense.type] = expense.amount + amount!!
            }
            else{
                hashMap[expense.type] = expense.amount
            }

        }

        val sortedMap = hashMap.toList().sortedByDescending { (k, v) -> v }.toMap()

        return sortedMap
    }

    fun valueToDiagramValue(values: ArrayList<Float>, totalExpense: Float): ArrayList<Float> {
        val percentValues = arrayListOf(0f, 0f, 0f, 0f, 0f, 0f)
        for (i in 0..5) {
            percentValues[i] = (values[i] * 360)/totalExpense
        }
        return percentValues
    }

    fun getMostExpensiveCategories(map: Map<String, Float>): List<String> {

        return map.keys.toList().slice(0..4)
    }

    fun getMostExpensiveValues(map: Map<String, Float>): ArrayList<Float> {
        val values = arrayListOf(0f, 0f, 0f, 0f, 0f, 0f)
        for (i in 0..4) {
            values[i] = map.values.elementAt(i)
        }
        for (i in 5 until map.values.size) {
            values[5] += map.values.elementAt(i)
        }
        return values
    }
}