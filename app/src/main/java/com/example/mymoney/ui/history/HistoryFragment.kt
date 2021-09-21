package com.example.mymoney.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mymoney.R
import com.example.mymoney.adapters.MoneyAdapter
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentHistoryBinding
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.models.MoneyModel
import com.example.mymoney.util.Constants.Companion.THIS_MONTH
import com.example.mymoney.util.Constants.Companion.THIS_WEEK
import com.example.mymoney.util.Constants.Companion.THIS_YEAR
import com.example.mymoney.util.Constants.Companion.TODAY
import com.example.mymoney.util.Constants.Companion.YESTERDAY
import com.example.mymoney.viewmodel.MainViewModel
import com.example.mymoney.viewmodel.SortViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { MoneyAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var sortViewModel: SortViewModel
    private var sortCategory: String? = null

    private var cardId: Int? = null
    private var sortId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        sortViewModel = ViewModelProvider(this).get(SortViewModel::class.java)


        mainViewModel.readMoney.observe(viewLifecycleOwner, { cards ->
            sortViewModel.readSortCardType.asLiveData().observe(viewLifecycleOwner, {
                cardId = it.cardId
                sortId = it.sortId

                if (cardId != null && sortId != null) {
                    adapter.setData(chooseSort(cards[cardId!!], sortId!!))
                    binding.cardName.text = cards[cardId!!].name
                }
                binding.sortCategory.text = sortCategory
            })
        })

        setUpRecyclerView()

        binding.sortFab.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_sortFragment)
        }

        return binding.root
    }

    private fun chooseSort(moneyEntity: MoneyEntity, sortId: Int): ArrayList<ExpenseModel> {
        var expenses = arrayListOf<ExpenseModel>()
        when(sortId) {
            0 -> {
                expenses = sortViewModel.sortToday(moneyEntity)
                sortCategory = TODAY
            }
            1 -> {
                expenses = sortViewModel.sortYesterday(moneyEntity)
                sortCategory = YESTERDAY
            }
            2 -> {
                expenses = sortViewModel.sortWeek(moneyEntity)
                sortCategory = THIS_WEEK
            }
            3 -> {
                expenses = sortViewModel.sortMonth(moneyEntity)
                sortCategory = THIS_MONTH
            }
            4 -> {
                expenses = sortViewModel.sortYear(moneyEntity)
                sortCategory = THIS_YEAR
            }
        }
        return expenses
    }


    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}