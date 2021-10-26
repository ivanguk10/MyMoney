package com.example.mymoney.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymoney.database.DataStoreRepository
import com.example.mymoney.database.entities.ExpenseEntity
import com.example.mymoney.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
): AndroidViewModel(application) {


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

    val readMonth = dataStoreRepository.readMonth

    fun saveMonth(monthId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMonth(monthId)
        }
    }

    private suspend fun getExpenses(): List<ExpenseEntity> {
        return repository.local.readExpenses()
    }

    fun insertExpense(expenseEntity: ExpenseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertExpenseEntity(expenseEntity)
        }
    }

    fun getMonthExpenses(monthId: Int) {
        val newList: ArrayList<ExpenseEntity> = arrayListOf()
        val now = currentMonth(monthId)
        val start = now.withDayOfMonth(1)
        val end = now.withDayOfMonth(now.lengthOfMonth())

        viewModelScope.launch(Dispatchers.IO) {
            val expenses = getExpenses()

            withContext(Dispatchers.Default) {
                expenses.forEach {
                    val date = stringToLocaleDate(it.date)
                    if (date == start || date == end || date.isAfter(start) && date.isBefore(end)) {
                        newList.add(it)
                    }
                }
                sortByTypeAndAmount(newList)
            }
        }
    }

    fun getYearExpenses() {
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
                sortByTypeAndAmount(newList)
            }
        }
    }

    private fun sortByTypeAndAmount(expenses: ArrayList<ExpenseEntity>) {
        val hashMap = HashMap<String, Float>()

        expenses.forEach { expense ->
            if (hashMap.containsKey(expense.type)) {
                val amount = hashMap[expense.type]
                hashMap[expense.type] = expense.amount + amount!!
            } else{
                hashMap[expense.type] = expense.amount
            }
        }
        Log.i("expense", expenses.toString())
        val sortedMap = hashMap.toList().sortedByDescending { (k, v) -> v }.toMap()
        getMostExpensiveValues(sortedMap)
        getMostExpensiveCategories(sortedMap)
    }

    private fun getMostExpensiveValues(map: Map<String, Float>) {
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
        _totalExpense.postValue(total)
        Log.i("biggestExpenses", biggestExpenses.toString())
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

    private fun currentMonth(monthId: Int): LocalDate {
        val year = LocalDate.now().year
        var now = LocalDate.now()
        when (monthId) {
            1 -> now = LocalDate.of(year, 1, 15)
            2 -> now = LocalDate.of(year, 2, 15)
            3 -> now = LocalDate.of(year, 3, 15)
            4 -> now = LocalDate.of(year, 4, 15)
            5 -> now = LocalDate.of(year, 5, 15)
            6 -> now = LocalDate.of(year, 6, 15)
            7 -> now = LocalDate.of(year, 7, 15)
            8 -> now = LocalDate.of(year, 8, 15)
            9 -> now = LocalDate.of(year, 9, 15)
            10 -> now = LocalDate.of(year, 10, 15)
            11 -> now = LocalDate.of(year, 11, 15)
            12 -> now = LocalDate.of(year, 12, 15)
        }
        return now
    }


    private fun stringToLocaleDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return LocalDate.parse(dateString.dropLast(6), formatter)
    }
}