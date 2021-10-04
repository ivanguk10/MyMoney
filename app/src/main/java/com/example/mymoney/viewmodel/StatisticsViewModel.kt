package com.example.mymoney.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
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

    private var _map: MutableLiveData<Map<String, Float>> = MutableLiveData()
    val map: LiveData<Map<String, Float>>
        get() = _map

    private var _biggestExpenses: MutableLiveData<ArrayList<Float>> = MutableLiveData(
        arrayListOf(0f, 0f, 0f, 0f, 0f, 0f)
    )
    val biggestExpenses: LiveData<ArrayList<Float>>
        get() = _biggestExpenses

    private var _biggestCategories: MutableLiveData<List<String>> = MutableLiveData()
    val biggestCategories: LiveData<List<String>>
        get() = _biggestCategories

    private var _monthExpenses: MutableLiveData<ArrayList<ExpenseEntity>> = MutableLiveData()
    val monthExpenses: LiveData<ArrayList<ExpenseEntity>>
        get() = _monthExpenses

    private var _totalExpense: MutableLiveData<Float> = MutableLiveData()
    val totalExpense : LiveData<Float>
        get() = _totalExpense

    private var _diagramValues: MutableLiveData<ArrayList<Float>> = MutableLiveData(
        arrayListOf(0f, 0f, 0f, 0f, 0f, 0f)
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

    suspend fun getMonthExpenses(monthId: Int): ArrayList<ExpenseEntity> {
        val newList: ArrayList<ExpenseEntity> = arrayListOf()
        val now = currentMonth(monthId)
        val start = now.withDayOfMonth(1)
        val end = now.withDayOfMonth(now.lengthOfMonth())
        val expenses = getExpenses()

        expenses.forEach {
            val date = stringToLocaleDate(it.date)
            if (date == start || date == end || date.isAfter(start) && date.isBefore(end)) {
                newList.add(it)
            }
        }
        _monthExpenses.value = newList
        _biggestExpenses.value = arrayListOf(0f, 0f, 0f, 0f, 0f, 0f)
        _totalExpense.value = 0f
        return newList
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

    fun sortByTypeAndAmountMonth() {
        val hashMap = HashMap<String, Float>()
        var total = 0f
        _monthExpenses.value?.forEach { expense ->
            if (hashMap.containsKey(expense.type)) {
                val amount = hashMap[expense.type]
                hashMap[expense.type] = expense.amount + amount!!
            } else{
                hashMap[expense.type] = expense.amount
            }
            total += expense.amount
            _totalExpense.value = total
        }
        Log.v("expense", _monthExpenses.value.toString())
        val sortedMap = hashMap.toList().sortedByDescending { (k, v) -> v }.toMap()
        _map.value = sortedMap
        getMostExpensiveValues()
        valueToDiagramValue()
        getMostExpensiveCategories()
    }

    private fun valueToDiagramValue() {

        val percentValues = arrayListOf(0f, 0f, 0f, 0f, 0f, 0f)
        val values = _biggestExpenses.value
        val total = _totalExpense.value
        if (values != null && total != null) {
            for (i in 0..5) {
                percentValues[i] = (values[i] * 360)/total
            }
        }

        _diagramValues.value = percentValues
        //_biggestExpenses.value = arrayListOf(0f, 0f, 0f, 0f, 0f, 0f)
    }

    private fun getMostExpensiveCategories() {

        _biggestCategories.value = _map.value?.keys?.toList()?.slice(0..4)
    }

    private fun getMostExpensiveValues() {
        val values = _biggestExpenses.value
        if (_map.value != null) {
            for (i in 0..4) {
                values!![i] = _map.value!!.values.elementAt(i)
            }
        }
        if (_map.value != null) {
            for (i in 5 until _map.value!!.values.size) {
                values!![5] += _map.value!!.values.elementAt(i)
            }
        }
        _biggestExpenses.value = values

    }

    private fun stringToLocaleDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return LocalDate.parse(dateString.dropLast(6), formatter)
    }
}