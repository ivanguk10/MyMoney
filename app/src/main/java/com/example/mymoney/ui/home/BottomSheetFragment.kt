package com.example.mymoney.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mymoney.R
import com.example.mymoney.databinding.FragmentBottomSheetBinding
import com.example.mymoney.viewmodel.KeyboardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val keyboardViewModel: KeyboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

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


        keyboardViewModel.amount.observe(viewLifecycleOwner, {
            binding.valueTextView.text = it
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}