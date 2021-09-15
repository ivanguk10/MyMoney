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
import com.example.mymoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { MoneyAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moneyEntity: MoneyEntity
    private var listOfNames: ArrayList<String> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.readMoney.observe(viewLifecycleOwner, { cards ->

            val firstExpenseList = cards.first().money.expenses
            adapter.setData(firstExpenseList)

            cards.forEach {
                listOfNames.add(it.name)
            }
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_card_name, listOfNames)
            binding.autoCompleteTextView.setAdapter(arrayAdapter)

            binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
                val selectedItem = adapterView.getItemAtPosition(i).toString()
                //val selected = adapterView.selectedItemId.toString()
                Toast.makeText(requireContext(), i.toString(), Toast.LENGTH_SHORT).show()
                val expenseList: ArrayList<ExpenseModel>
                val card = cards[i]
                expenseList = card.money.expenses
                adapter.setData(expenseList)
            }
        })

        setUpRecyclerView()

        return binding.root
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