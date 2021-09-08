package com.example.mymoney.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.mymoney.R
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentBottomSheetBinding
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.models.MoneyModel
import com.example.mymoney.util.Constants.Companion.DEFAULT_TYPE
import com.example.mymoney.viewmodel.KeyboardViewModel
import com.example.mymoney.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val keyboardViewModel: KeyboardViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel

    private var expenseType = DEFAULT_TYPE
    private var expenseTypeId = 0

    private lateinit var moneyEntity: MoneyEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel.readExpenseType.asLiveData().observe(viewLifecycleOwner, { value ->
            expenseType = value.selectedType
            updateChip(value.selectedTypeId, binding.spendTypeChipGroup)
        })

        mainViewModel.readMoney.observe(viewLifecycleOwner, {
            moneyEntity = it.first()
        })

        binding.btn1.setOnClickListener { keyboardViewModel.addNumb("1") }
        binding.btn2.setOnClickListener { keyboardViewModel.addNumb("2") }
        binding.btn3.setOnClickListener { keyboardViewModel.addNumb("3") }
        binding.btn4.setOnClickListener { keyboardViewModel.addNumb("4") }
        binding.btn5.setOnClickListener { keyboardViewModel.addNumb("5") }
        binding.btn6.setOnClickListener { keyboardViewModel.addNumb("6") }
        binding.btn7.setOnClickListener { keyboardViewModel.addNumb("7") }
        binding.btn8.setOnClickListener { keyboardViewModel.addNumb("8") }
        binding.btn9.setOnClickListener { keyboardViewModel.addNumb("9") }
        binding.btn0.setOnClickListener { keyboardViewModel.addNumb("0") }
        binding.btnDel.setOnClickListener { keyboardViewModel.deleteNumb() }
        //binding.btnDone.setOnClickListener { expenseMoney() }

        binding.spendTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            expenseType = chip.text.toString()
            expenseTypeId = checkedId
        }

        keyboardViewModel.amount.observe(viewLifecycleOwner, {
            binding.valueTextView.text = it
        })

        binding.btnDone.setOnClickListener {
            mainViewModel.saveExpenseTypeTemp(expenseType, expenseTypeId)
            expenseMoney()
        }

        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                val chip = chipGroup.findViewById<Chip>(chipId)
                chip.isChecked = true
                chipGroup.requestChildFocus(chip, chip)
            } catch (e:Exception) {
                Log.d("bottomSheet", e.message.toString())
            }
        }
    }

    private fun expenseMoney() {
        val expenseList = moneyEntity.money.expenses
        val newExpense = binding.valueTextView.text.toString().toFloat()
        val newExpenseModel = ExpenseModel(
           newExpense,
           expenseType,
           getDate()
        )
        expenseList.add(newExpenseModel)

        val moneyAmount = moneyEntity.value - newExpense

        val moneyModel = MoneyModel(
            moneyEntity.money.incomes,
            expenseList
        )
        mainViewModel.updateMoneyEntity(
            MoneyEntity(
            1,
            moneyAmount,
            moneyModel
        ))
        findNavController().navigate(R.id.action_bottomSheetFragment_to_homeFragment)
    }

    private fun getDate(): String {

        //Be careful in the future with date format
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")

        return dateFormat.format(calendar.time)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}