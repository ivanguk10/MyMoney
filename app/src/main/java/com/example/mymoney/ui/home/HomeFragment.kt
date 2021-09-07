package com.example.mymoney.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mymoney.R
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentHomeBinding
import com.example.mymoney.util.Constants.Companion.MONEY_BUNDLE
import com.example.mymoney.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
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

        val moneyBundle = Bundle()
        //val fragmentTransaction = fragmentManager?.beginTransaction();
        mainViewModel.readMoney.observe(viewLifecycleOwner, { money ->
            binding.moneyCountTextView.text = money.first().value.toString()


            if (money != null) {
                moneyBundle.putString(MONEY_BUNDLE, money.first().money.toString())
                arguments = moneyBundle
            }

        })

        binding.addMoneyFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bottomSheetIncomeFragment)
        }

        binding.subtractMoneyFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bottomSheetFragment)
        }




        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}