package com.example.mymoney.ui.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mymoney.R
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentBottomSheetIncomeBinding
import com.example.mymoney.models.IncomeModel
import com.example.mymoney.models.MoneyModel
import com.example.mymoney.observeOnce
import com.example.mymoney.viewmodel.KeyboardViewModel
import com.example.mymoney.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomSheetIncomeFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetIncomeBinding? = null
    private val binding get() = _binding!!

    private val keyboardViewModel: KeyboardViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel

    private lateinit var moneyEntity: MoneyEntity
    private lateinit var incomeList: ArrayList<IncomeModel>
    private var moneyAmount = 0f

    //private val args: BottomSheetIncomeFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBottomSheetIncomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.btn1In.setOnClickListener { keyboardViewModel.addNumb("1") }
        binding.btn2In.setOnClickListener { keyboardViewModel.addNumb("2") }
        binding.btn3In.setOnClickListener { keyboardViewModel.addNumb("3") }
        binding.btn4In.setOnClickListener { keyboardViewModel.addNumb("4") }
        binding.btn5In.setOnClickListener { keyboardViewModel.addNumb("5") }
        binding.btn6In.setOnClickListener { keyboardViewModel.addNumb("6") }
        binding.btn7In.setOnClickListener { keyboardViewModel.addNumb("7") }
        binding.btn8In.setOnClickListener { keyboardViewModel.addNumb("8") }
        binding.btn9In.setOnClickListener { keyboardViewModel.addNumb("9") }
        binding.btn0In.setOnClickListener { keyboardViewModel.addNumb("0") }
        binding.btnDelIn.setOnClickListener { keyboardViewModel.deleteNumb() }
        binding.btnDoneIn.setOnClickListener { addMoneyAmount() }


        keyboardViewModel.amount.observe(viewLifecycleOwner, {
            binding.valueTextViewIn.text = it
        })

        mainViewModel.readMoney.observe(viewLifecycleOwner, { money ->
            moneyEntity = money.first()
        })

        return binding.root
    }

    private fun addMoneyAmount() {

        incomeList = moneyEntity.money.incomes
        val incomeValue = binding.valueTextViewIn.text.toString().toFloat()
        val income = IncomeModel(incomeValue)

        incomeList.add(income)
        moneyAmount = moneyEntity.value + incomeValue

        val moneyModel = MoneyModel(
            incomeList,
            moneyEntity.money.expenses
        )
        mainViewModel.updateMoneyEntity(MoneyEntity(
            1,
            moneyAmount,
            moneyModel
        ))
        findNavController().navigate(R.id.action_bottomSheetIncomeFragment_to_homeFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}