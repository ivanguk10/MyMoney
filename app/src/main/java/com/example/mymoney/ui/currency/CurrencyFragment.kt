package com.example.mymoney.ui.currency

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.mymoney.R
import com.example.mymoney.databinding.FragmentCurrencyBinding
import com.example.mymoney.util.NetworkResult
import com.example.mymoney.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyFragment : Fragment() {

    private var _binding: FragmentCurrencyBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCurrencyBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel.getCurrencies("USD")
        mainViewModel.currenciesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let { binding.tv.text = it.toString() }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                }
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}