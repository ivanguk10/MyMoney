package com.example.mymoney.ui.statistics

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mymoney.databinding.FragmentWeekStatiscticsBinding
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.setWidthPercent
import com.example.mymoney.viewmodel.MainViewModel
import com.example.mymoney.viewmodel.SortViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint
class WeekStatisticsFragment : DialogFragment() {

    private var _binding: FragmentWeekStatiscticsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private val sortViewModel: SortViewModel by viewModels()
    private var listOfExpenses: ArrayList<ExpenseModel> = arrayListOf()
    private var listOfTypes: ArrayList<String> = arrayListOf()
    private var totalExpense = 0f
    private var totalIncome = 0f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekStatiscticsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        mainViewModel.readMoney.observe(viewLifecycleOwner, { cards ->

            cards.forEach { card ->
                val list = sortViewModel.sortWeek(card)
                listOfExpenses.addAll(list)
                card.money.incomes.forEach { income ->
                    totalIncome += income.amount
                }
                card.money.expenses.forEach { expense ->
                    totalExpense += expense.amount
                }
            }
            val sortedMap = sortViewModel.sortByTypeAndAmount(listOfExpenses)
            val values = sortViewModel.getMostExpensiveValues(sortedMap)
            val categories = sortViewModel.getMostExpensiveCategories(sortedMap)
            val diagramValues = sortViewModel.valueToDiagramValue(values, totalExpense)

            binding.pieChart.setExp1(diagramValues[0])
            binding.pieChart.setExp2(diagramValues[1])
            binding.pieChart.setExp3(diagramValues[2])
            binding.pieChart.setExp4(diagramValues[3])
            binding.pieChart.setExp5(diagramValues[4])
            binding.pieChart.setExp6(diagramValues[5])


            binding.category1.text = categories.elementAt(0)
            binding.category2.text = categories.elementAt(1)
            binding.category3.text = categories.elementAt(2)
            binding.category4.text = categories.elementAt(3)
            binding.category5.text = categories.elementAt(4)

            binding.incomeValue.text = totalIncome.toString()
            binding.expenseValue.text = totalExpense.toString()


            Toast.makeText(requireContext(), values.toString(), Toast.LENGTH_LONG).show()
            binding.expense1Value.text = values[0].toString()
            binding.expense2Value.text = values[1].toString()
            binding.expense3Value.text = values[2].toString()
            binding.expense4Value.text = values[3].toString()
            binding.expense5Value.text = values[4].toString()
            binding.expense6Value.text = values[5].toString()
        })

        return binding.root
    }



    override fun onStart() {
        super.onStart()
        setWidthPercent(96, 90)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}