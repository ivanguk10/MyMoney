package com.example.mymoney.ui.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mymoney.R
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentFirstScreenBinding
import com.example.mymoney.databinding.FragmentSecondScreenBinding
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.models.IncomeModel
import com.example.mymoney.models.MoneyModel
import com.example.mymoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondScreen : Fragment() {

    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!

    val color = R.color.purpleDark

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFirstMoneyEntity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondScreenBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.doneTextView.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_homeFragment)
            onBoardingFinished()
        }

        Toast.makeText(requireContext(), color.toString(), Toast.LENGTH_SHORT).show()


        return binding.root
    }

    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("finished", true)
        editor.apply()
    }

    private fun addFirstMoneyEntity() {
        val money = MoneyEntity(
            0,
            name = "Money",
            0f,
            money = MoneyModel(arrayListOf<IncomeModel>(), arrayListOf<ExpenseModel>()),
            R.color.purpleDark
        )
        mainViewModel.insertMoneyEntity(money)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}