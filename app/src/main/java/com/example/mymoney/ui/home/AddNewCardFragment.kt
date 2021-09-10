package com.example.mymoney.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mymoney.R
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentAddNewCardBinding
import com.example.mymoney.models.ExpenseModel
import com.example.mymoney.models.IncomeModel
import com.example.mymoney.models.MoneyModel
import com.example.mymoney.setFullScreen
import com.example.mymoney.setWidthPercent
import com.example.mymoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddNewCardFragment : DialogFragment() {

    private var _binding: FragmentAddNewCardBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    private var cardColor = R.color.purpleDark

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddNewCardBinding.inflate(layoutInflater, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.insertNewCard.setOnClickListener {
            insertNewCard()
            findNavController().navigate(R.id.action_addNewCardFragment_to_homeFragment)
        }

        binding.goldButton.setOnClickListener {
            binding.moneyCard.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(),
                    R.color.gold)
            )
            cardColor = R.color.gold
        }
        binding.purpleButton.setOnClickListener {
            binding.moneyCard.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(),
                    R.color.purpleDark)
            )
            cardColor = R.color.purpleDark
        }
        binding.silverButton.setOnClickListener {
            binding.moneyCard.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(),
                    R.color.silver)
            )
            cardColor = R.color.silver
        }
        binding.blueButton.setOnClickListener {
            binding.moneyCard.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(),
                    R.color.pastelBlue)
            )
            cardColor = R.color.pastelBlue
        }
        binding.orangeButton.setOnClickListener {
            binding.moneyCard.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(),
                    R.color.pastel)
            )
            cardColor = R.color.pastel
        }
        binding.greenButton.setOnClickListener {
            binding.moneyCard.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(),
                    R.color.pastelGreen)
            )
            cardColor = R.color.pastelGreen
        }

        return binding.root
    }


    private fun insertNewCard() {
        val moneyEntity = MoneyEntity(
            0,
            binding.moneyNameEditText.text.toString(),
            binding.moneyCountEditText.text.toString().toFloat(),
            money = MoneyModel(
                arrayListOf<IncomeModel>(),
                arrayListOf<ExpenseModel>()
            ),
            cardColor
        )
        mainViewModel.insertMoneyEntity(moneyEntity)
    }

    override fun onStart() {
        super.onStart()
        setWidthPercent(96, 70)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}