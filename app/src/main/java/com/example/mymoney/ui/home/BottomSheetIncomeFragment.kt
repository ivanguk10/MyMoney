package com.example.mymoney.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mymoney.databinding.FragmentBottomSheetIncomeBinding
import com.example.mymoney.viewmodel.KeyboardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetIncomeFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetIncomeBinding? = null
    private val binding get() = _binding!!
    private val keyboardViewModel: KeyboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBottomSheetIncomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

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


        keyboardViewModel.amount.observe(viewLifecycleOwner, {
            binding.valueTextViewIn.text = it
        })

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}