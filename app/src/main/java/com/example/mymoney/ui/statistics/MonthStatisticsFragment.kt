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
    private var totalExpense = 0f
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
                lifecycleScope.launch {
                    statisticsViewModel.getMonthExpenses(month!!)
                    statisticsViewModel.sortByTypeAndAmountMonth()
                }
            }
        })

        statisticsViewModel.totalExpense.observe(viewLifecycleOwner, { total ->
            binding.expenseValue.text = total.toString()
        })

        statisticsViewModel.biggestExpenses.observe(viewLifecycleOwner, { values ->
            binding.expense1Value.text = values[0].toString()
            binding.expense2Value.text = values[1].toString()
            binding.expense3Value.text = values[2].toString()
            binding.expense4Value.text = values[3].toString()
            binding.expense5Value.text = values[4].toString()
            binding.expense6Value.text = values[5].toString()
        })
        statisticsViewModel.diagramValues.observe(viewLifecycleOwner, { diagramValues ->
            binding.pieChart.setExp1(diagramValues[0])
            binding.pieChart.setExp2(diagramValues[1])
            binding.pieChart.setExp3(diagramValues[2])
            binding.pieChart.setExp4(diagramValues[3])
            binding.pieChart.setExp5(diagramValues[4])
            binding.pieChart.setExp6(diagramValues[5])
        })

        statisticsViewModel.biggestCategories.observe(viewLifecycleOwner, { categories ->
            binding.category1.text = categories.elementAt(0)
            binding.category2.text = categories.elementAt(1)
            binding.category3.text = categories.elementAt(2)
            binding.category4.text = categories.elementAt(3)
            binding.category5.text = categories.elementAt(4)
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