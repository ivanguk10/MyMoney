package com.example.mymoney.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoney.R
import com.example.mymoney.adapters.MoneyAdapter
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentHistoryBinding
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.models.MoneyModel
import com.example.mymoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { MoneyAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moneyEntity: MoneyEntity
    private var listOfNames: ArrayList<String> = arrayListOf()
    private var cardPosition = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.readMoney.observe(viewLifecycleOwner, { cards ->

            val firstExpenseList = cards.first().money.expenses
            adapter.setData(sortForLastWeek(firstExpenseList))
            binding.cardName.text = cards.first().name

            cards.forEach {
                listOfNames.add(it.name)
            }

            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_card_name, listOfNames)
            binding.autoCompleteTextView.setAdapter(arrayAdapter)

            binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
                Toast.makeText(requireContext(), i.toString(), Toast.LENGTH_SHORT).show()
                val expenseList = cards[i].money.expenses
                adapter.setData(expenseList)
                binding.cardName.text = cards[i].name
                cardPosition = i
            }

            val arrayList = arrayListOf("For last week", "For last month", "For last year", "For all time")
            val timeArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_card_name, arrayList)
            binding.autoCompleteTimeTextView.setAdapter(timeArrayAdapter)
            binding.autoCompleteTimeTextView.setOnItemClickListener { adapterView, view, i, l ->
                val expenseList = cards[cardPosition].money.expenses
                when(i) {
                    0 -> {
                        adapter.setData(sortForLastWeek(expenseList))
                    }
                }
            }
        })


        setUpRecyclerView()

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val a = dateFormat.format(calendar.time)
        //Toast.makeText(requireContext(), a.toString(), Toast.LENGTH_SHORT).show()



        return binding.root
    }

    private fun sortForLastWeek(listOfExpenses: ArrayList<ExpenseModel>): ArrayList<ExpenseModel> {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val newList: ArrayList<ExpenseModel> = arrayListOf()
        listOfExpenses.forEach {
            val date = simpleDateFormat.parse(it.date)
            if (date.date + 7 > calendar.time.date) {
                newList.add(it)
            }
        }
        return newList
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}