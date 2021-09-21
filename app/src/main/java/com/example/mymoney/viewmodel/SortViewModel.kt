package com.example.mymoney.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoney.database.DataStoreRepository
import com.example.mymoney.database.SortCardType
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.repository.Repository
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

    private lateinit var sortCardType: SortCardType
    val readSortCardType = dataStoreRepository.readSortType

    fun saveSortCardType(cardId: Int, sortId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveSortType(
                cardId,
                sortId
            )
        }
    }
//    fun saveSortCardTypeTemp(
//        cardId: Int,
//        sortId: Int
//    ) {
//        sortCardType = SortCardType(
//            cardId,
//            sortId
//        )
//    }

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
}