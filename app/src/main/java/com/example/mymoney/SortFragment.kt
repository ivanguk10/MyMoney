package com.example.mymoney

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentSortBinding
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
                val expenseList = cards[i].money.expenses
                moneyEntity = cards[i]
            }
        })

        binding.today.setOnClickListener {
            sortChoice = 1
            Toast.makeText(requireContext(), sortChoice.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.yesterday.setOnClickListener {
            sortChoice = 2
            Toast.makeText(requireContext(), sortChoice.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.week.setOnClickListener {
            sortChoice = 3
            Toast.makeText(requireContext(), sortChoice.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.month.setOnClickListener {
            sortChoice = 4
            Toast.makeText(requireContext(), sortChoice.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.year.setOnClickListener {
            sortChoice = 5
            Toast.makeText(requireContext(), sortChoice.toString(), Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }


    private fun sortWeek(money: MoneyEntity): ArrayList<ExpenseModel> {

        val newList: ArrayList<ExpenseModel> = arrayListOf()

        money.money.expenses.forEach {
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

    private fun stringToLocaleDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return parse(dateString, formatter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}