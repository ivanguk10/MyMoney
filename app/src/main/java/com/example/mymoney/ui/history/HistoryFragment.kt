package com.example.mymoney.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoney.MoneyAdapter
import com.example.mymoney.R
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentHistoryBinding
import com.example.mymoney.models.MoneyModel
import com.example.mymoney.util.Constants.Companion.MONEY_BUNDLE
import com.example.mymoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { MoneyAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moneyEntity: MoneyEntity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.readMoney.observe(viewLifecycleOwner, {
            moneyEntity = it.first()
            val expenseList = moneyEntity.money.expenses

            adapter.setData(expenseList)
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