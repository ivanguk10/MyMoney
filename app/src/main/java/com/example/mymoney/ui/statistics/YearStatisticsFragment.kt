package com.example.mymoney.ui.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mymoney.R
import com.example.mymoney.databinding.FragmentYearStatisticsBinding
import com.example.mymoney.viewmodel.YearStatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YearStatisticsFragment : Fragment() {

    private var _binding: FragmentYearStatisticsBinding? = null
    private val binding get() = _binding!!
    private val yearStatisticsViewModel: YearStatisticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentYearStatisticsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        yearStatisticsViewModel.getYearExpenses()

        yearStatisticsViewModel.totalExpense.observe(viewLifecycleOwner, { total ->
            binding.expenseValue.text = total.toString()
        })

        yearStatisticsViewModel.biggestExpenses.observe(viewLifecycleOwner, { values ->
            val expensesTv = arrayListOf(binding.expense1Value, binding.expense2Value,
                binding.expense3Value, binding.expense4Value, binding.expense5Value,
                binding.expense6Value
            )

            for (i in 0 until values.size) {
                expensesTv[i].text = values[i].toString()
            }
        })
        yearStatisticsViewModel.diagramValues.observe(viewLifecycleOwner, { diagramValues ->

            binding.pieChart.setExpenseValues(diagramValues)
        })

        yearStatisticsViewModel.biggestCategories.observe(viewLifecycleOwner, { categories ->
            val categoriesTv = arrayListOf(binding.category1, binding.category2, binding.category3,
                binding.category4, binding.category5
            )

            for (i in categories.indices) {
                categoriesTv[i].text = categories.elementAt(i)
            }

        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}