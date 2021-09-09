package com.example.mymoney.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoney.adapters.CardAdapter
import com.example.mymoney.R
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentHomeBinding
import com.example.mymoney.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val cardAdapter: CardAdapter by lazy { CardAdapter() }
    private var moneyEntity: MoneyEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setUpRecyclerView()

        mainViewModel.readMoney.observe(viewLifecycleOwner, { money ->

            cardAdapter.setData(money)
        })

        binding.addMoneyFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bottomSheetIncomeFragment)
        }

        binding.subtractMoneyFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bottomSheetFragment)
        }

        binding.addNewCardBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNewCardFragment)
        }




        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.homeRecyclerView.adapter = cardAdapter
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}