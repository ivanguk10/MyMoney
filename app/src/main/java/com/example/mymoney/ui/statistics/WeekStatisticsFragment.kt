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
import com.example.mymoney.databinding.FragmentWeekStatiscticsBinding
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.setWidthPercent
import com.example.mymoney.viewmodel.MainViewModel
import com.example.mymoney.viewmodel.SortViewModel
import dagger.hilt.android.AndroidEntryPoint
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

    var expense1 = 0f
    var expense2 = 0f
    var expense3 = 0f
    var expense4 = 0f
    var expense5 = 0f
    var expense6 = 0f

    var exp1Percent = 0f
    var exp2Percent = 0f
    var exp3Percent = 0f
    var exp4Percent = 0f
    var exp5Percent = 0f
    var exp6Percent = 0f

    var category1 = ""
    var category2 = ""
    var category3 = ""
    var category4 = ""
    var category5 = ""



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
                Toast.makeText(requireContext(), sortByTypeAndAmount(listOfExpenses).toString(), Toast.LENGTH_LONG).show()
            }
            listOfExpenses.forEach { expense ->
                if (!listOfTypes.contains(expense.type)) {
                    listOfTypes.add(expense.type)
                }
            }
            sortValues(sortByTypeAndAmount(listOfExpenses))
            binding.pieChart.setExp1(exp1Percent)
            binding.pieChart.setExp2(exp2Percent)
            binding.pieChart.setExp3(exp3Percent)
            binding.pieChart.setExp4(exp4Percent)
            binding.pieChart.setExp5(exp5Percent)
            binding.pieChart.setExp6(exp6Percent)

            binding.category1.text = category1
            binding.category2.text = category2
            binding.category3.text = category3
            binding.category4.text = category4
            binding.category5.text = category5

            binding.incomeValue.text = totalIncome.toString()
            binding.expenseValue.text = totalExpense.toString()

            binding.expense1Value.text = expense1.toString()
            binding.expense2Value.text = expense2.toString()
            binding.expense3Value.text = expense3.toString()
            binding.expense4Value.text = expense4.toString()
            binding.expense5Value.text = expense5.toString()
            binding.expense6Value.text = expense6.toString()
        })


        return binding.root
    }

    private fun sortByTypeAndAmount(expenses: ArrayList<ExpenseModel>): Map<String, Float> {
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

    private fun sortValues(map: Map<String, Float>): ArrayList<Float> {
        map.keys.forEach {
            if (category1 == "") {
                category1 = it
            }
            else if (category2 == "") {
                category2 = it
            }
            else if (category3 == "") {
                category3 = it
            }
            else if (category4 == "") {
                category4 = it
            }
            else if (category5 == "") {
                category5 = it
            }
        }

        map.values.forEach {
            if (expense1 == 0f) {
                expense1 = it
                exp1Percent = (expense1 * 360)/totalExpense
            } else if (expense2 == 0f) {
                expense2 = it
                exp2Percent = (expense2 * 360)/totalExpense
            } else if (expense3 == 0f) {
                expense3 = it
                exp3Percent = (expense3 * 360)/totalExpense
            } else if (expense4 == 0f) {
                expense4 = it
                exp4Percent = (expense4 * 360)/totalExpense
            } else if (expense5 == 0f) {
                expense5 = it
                exp5Percent = (expense5 * 360)/totalExpense
            } else {
                expense6 += it
                exp6Percent = (expense6 * 360)/totalExpense
            }
        }

        return arrayListOf(expense1, expense2, expense3, expense4, expense5, expense6)
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