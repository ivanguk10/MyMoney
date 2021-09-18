package com.example.mymoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentSortBinding
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDate.parse
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SortFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSortBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private var listOfNames: ArrayList<String> = arrayListOf()
    private var moneyEntity: MoneyEntity? = null
    private var sortChoice = 0
    private var sortedList: ArrayList<ExpenseModel> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSortBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.readMoney.observe(viewLifecycleOwner, { cards ->

            cards.forEach {
                listOfNames.add(it.name)
            }

            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_card_name, listOfNames)
            binding.autoCompleteTextView.setAdapter(arrayAdapter)

            binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
                Toast.makeText(requireContext(), i.toString(), Toast.LENGTH_SHORT).show()
                moneyEntity = cards[i]
            }
        })

        binding.today.setOnClickListener {
            sortChoice = 1
            Toast.makeText(requireContext(), sortChoice.toString(), Toast.LENGTH_SHORT).show()
            selectedItem(sortChoice)
            sortedList = sortToday(moneyEntity)
        }

        binding.yesterday.setOnClickListener {
            sortChoice = 2
            Toast.makeText(requireContext(), sortChoice.toString(), Toast.LENGTH_SHORT).show()
            selectedItem(sortChoice)
            sortedList = sortYesterday(moneyEntity)
        }

        binding.week.setOnClickListener {
            sortChoice = 3
            Toast.makeText(requireContext(), sortChoice.toString(), Toast.LENGTH_SHORT).show()
            sortedList = sortWeek(moneyEntity)
            selectedItem(sortChoice)
        }

        binding.month.setOnClickListener {
            sortChoice = 4
            Toast.makeText(requireContext(), sortChoice.toString(), Toast.LENGTH_SHORT).show()
            sortedList = sortMonth(moneyEntity)
            selectedItem(sortChoice)
        }

        binding.year.setOnClickListener {
            sortChoice = 5
            Toast.makeText(requireContext(), sortChoice.toString(), Toast.LENGTH_SHORT).show()
            selectedItem(sortChoice)
        }

        binding.sortButton.setOnClickListener {
            val action = SortFragmentDirections.actionSortFragmentToHistoryFragment(sortedList.toTypedArray())
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun sortWeek(money: MoneyEntity?): ArrayList<ExpenseModel> {

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

    private fun sortMonth(money: MoneyEntity?): ArrayList<ExpenseModel> {
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

    private fun sortToday(money: MoneyEntity?): ArrayList<ExpenseModel> {
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

    private fun sortYesterday(money: MoneyEntity?): ArrayList<ExpenseModel> {
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

    private fun stringToLocaleDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return parse(dateString.dropLast(6), formatter)
    }

    private fun clickedSort(textView: TextView, imageView: ImageView, flag: Boolean) {
        if (flag) {
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.darkPurple))
            imageView.visibility = View.VISIBLE
        }
        else {
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.lightGray))
            imageView.visibility = View.INVISIBLE
        }
    }

    private fun selectedItem(choice: Int) {
        if (choice == 1)
            clickedSort(binding.todayTv, binding.todayCheck, true)
        else
            clickedSort(binding.todayTv, binding.todayCheck, false)

        if (choice == 2)
            clickedSort(binding.yesterdayTv, binding.yesterdayCheck, true)
        else
            clickedSort(binding.yesterdayTv, binding.yesterdayCheck, false)

        if (choice == 3)
            clickedSort(binding.weekTv, binding.weekCheck, true)
        else
            clickedSort(binding.weekTv, binding.weekCheck, false)

        if (choice == 4)
            clickedSort(binding.monthTv, binding.monthCheck, true)
        else
            clickedSort(binding.monthTv, binding.monthCheck, false)

        if (choice == 5)
            clickedSort(binding.yearTv, binding.yearCheck, true)
        else
            clickedSort(binding.yearTv, binding.yearCheck, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}