package com.example.mymoney.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoney.database.entities.ExpenseEntity
import com.example.mymoney.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class YearStatisticsViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {


    private var _biggestExpenses: MutableLiveData<ArrayList<Float>> = MutableLiveData(
        arrayListOf()
    )
    val biggestExpenses: LiveData<ArrayList<Float>>
        get() = _biggestExpenses

    private var _biggestCategories: MutableLiveData<List<String>> = MutableLiveData()
    val biggestCategories: LiveData<List<String>>
        get() = _biggestCategories


    private var _totalExpense: MutableLiveData<Float> = MutableLiveData()
    val totalExpense : LiveData<Float>
        get() = _totalExpense

    private var _diagramValues: MutableLiveData<ArrayList<Float>> = MutableLiveData(
        arrayListOf()
    )
    val diagramValues: LiveData<ArrayList<Float>>
        get() = _diagramValues

    private suspend fun getExpenses(): List<ExpenseEntity> {
        return repository.local.readExpenses()
    }

    fun getYearExpenses(): ArrayList<ExpenseEntity> {
        val newList: ArrayList<ExpenseEntity> = arrayListOf()
        val now = LocalDate.now()
        val start = now.withDayOfYear(1)
        val end = now.withDayOfYear(now.lengthOfYear())
        viewModelScope.launch(Dispatchers.IO) {
            val expenses = getExpenses()

            withContext(Dispatchers.Default) {
                expenses.forEach {
                    val date = stringToLocaleDate(it.date)
                    if (date == start || date == end || date.isAfter(start) && date.isBefore(end)) {
                        newList.add(it)
                    }
                }
                sortByTypeAndAmountYear(newList)
            }
        }
        return newList
    }

    private fun sortByTypeAndAmountYear(yearExpenses: ArrayList<ExpenseEntity>) {
        val hashMap = HashMap<String, Float>()
        var total = 0f
        yearExpenses.forEach { expense ->
            if (hashMap.containsKey(expense.type)) {
                val amount = hashMap[expense.type]
                hashMap[expense.type] = expense.amount + amount!!
            } else{
                hashMap[expense.type] = expense.amount
            }
            total += expense.amount
            _totalExpense.postValue(total)
        }
        Log.i("expense", yearExpenses.toString())
        val sortedMap = hashMap.toList().sortedByDescending { (k, v) -> v }.toMap()
        getMostExpensiveValues(sortedMap)
        getMostExpensiveCategories(sortedMap)
    }

    private fun getMostExpensiveValues(map: Map<String, Float>): ArrayList<Float> {
        Log.i("TAG", map.toString())
        val biggestExpenses: ArrayList<Float>
        if (map.size >= 6 ) {
            val values = arrayListOf(0f, 0f, 0f, 0f, 0f, 0f,)

            for (i in 0..4) {
                values[i] = map.values.elementAt(i)
            }
            for (i in 5 until map.values.size) {
                values[5] += map.values.elementAt(i)
            }
            biggestExpenses = values
        }
        else {
            val values = arrayListOf<Float>()
            map.values.forEach {
                values.add(it)
            }
            biggestExpenses = values
        }
        val total = biggestExpenses.sum()
        valueToDiagramValue(biggestExpenses, total)
        _biggestExpenses.postValue(biggestExpenses)
        Log.i("biggestExpenses", biggestExpenses.toString())
        return biggestExpenses
    }

    private fun valueToDiagramValue(biggestExpenses: ArrayList<Float>, total: Float) {

        val percentValues = arrayListOf(0f, 0f, 0f, 0f, 0f, 0f,)
        for (i in 0 until biggestExpenses.size) {
            percentValues[i] = (biggestExpenses[i] * 360)/total
        }
        _diagramValues.postValue(percentValues)
    }

    private fun getMostExpensiveCategories(map: Map<String, Float>) {
        val biggestCategories: List<String>
        if (map.size >= 6) {
            biggestCategories = map.keys.toList().slice(0..4)
        }
        else {
            val size = map.keys.size
            biggestCategories = map.keys.toList().slice(0 until size)

        }
        _biggestCategories.postValue(biggestCategories)
        Log.i("biggestCategories", biggestCategories.toString())
    }




    private fun stringToLocaleDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return LocalDate.parse(dateString.dropLast(6), formatter)
    }
}