package com.example.mymoney.ui.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.mymoney.R
import com.example.mymoney.databinding.FragmentMonthStatisticsBinding
import com.example.mymoney.viewmodel.SortViewModel
import com.example.mymoney.viewmodel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MonthStatisticsFragment : Fragment() {

    private var _binding: FragmentMonthStatisticsBinding? = null
    private val binding get() = _binding!!
    private val statisticsViewModel: StatisticsViewModel by viewModels()
    private var month: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMonthStatisticsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        statisticsViewModel.readMonth.asLiveData().observe(viewLifecycleOwner, {
            month = it.monthId
            if (month != null) {
                statisticsViewModel.getMonthExpenses(month!!)
            }
        })

        statisticsViewModel.totalExpense.observe(viewLifecycleOwner, { total ->
            binding.expenseValue.text = total.toString()
        })

        statisticsViewModel.biggestExpenses.observe(viewLifecycleOwner, { values ->
            val expensesTv = arrayListOf(binding.expense1Value, binding.expense2Value,
                binding.expense3Value, binding.expense4Value, binding.expense5Value,
                binding.expense6Value
            )

            for (i in 0 until values.size) {
                expensesTv[i].text = values[i].toString()
            }
        })
        statisticsViewModel.diagramValues.observe(viewLifecycleOwner, { diagramValues ->

            binding.pieChart.setExpenseValues(diagramValues)
        })

        statisticsViewModel.biggestCategories.observe(viewLifecycleOwner, { categories ->
            val categoriesTv = arrayListOf(binding.category1, binding.category2, binding.category3,
                binding.category4, binding.category5
            )

            for (i in categories.indices) {
                categoriesTv[i].text = categories.elementAt(i)
            }

        })

        binding.january.setOnClickListener {
            statisticsViewModel.saveMonth(1)
        }
        binding.february.setOnClickListener {
            statisticsViewModel.saveMonth(2)
        }
        binding.march.setOnClickListener {
            statisticsViewModel.saveMonth(3)
        }
        binding.april.setOnClickListener {
            statisticsViewModel.saveMonth(4)
        }
        binding.may.setOnClickListener {
            statisticsViewModel.saveMonth(5)
        }
        binding.june.setOnClickListener {
            statisticsViewModel.saveMonth(6)
        }
        binding.july.setOnClickListener {
            statisticsViewModel.saveMonth(7)
        }
        binding.august.setOnClickListener {
            statisticsViewModel.saveMonth(8)
        }
        binding.september.setOnClickListener {
            statisticsViewModel.saveMonth(9)
        }
        binding.october.setOnClickListener {
            statisticsViewModel.saveMonth(10)
        }
        binding.november.setOnClickListener {
            statisticsViewModel.saveMonth(11)
        }
        binding.december.setOnClickListener {
            statisticsViewModel.saveMonth(12)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}