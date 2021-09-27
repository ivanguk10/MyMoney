package com.example.mymoney.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.mymoney.R
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentSortBinding
import com.example.mymoney.viewmodel.MainViewModel
import com.example.mymoney.viewmodel.SortViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SortFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSortBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private var listOfNames: ArrayList<String> = arrayListOf()
    private var moneyEntity: MoneyEntity? = null
    private var moneyEntityId: Int? = null
    private var sortChoice: Int? = null
    private val sortViewModel: SortViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSortBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)



        mainViewModel.readMoney.observe(viewLifecycleOwner, { cards ->

            sortViewModel.readSortCardType.asLiveData().observe(viewLifecycleOwner, {
                sortChoice = it.sortId
                moneyEntityId = it.cardId
                selectedItem(sortChoice!!)
                binding.autoCompleteTextView.setText(cards[moneyEntityId!!].name)
            })

            cards.forEach {
                listOfNames.add(it.name)
            }

            val arrayAdapter = ArrayAdapter(requireContext(),
                R.layout.dropdown_card_name, listOfNames)
            binding.autoCompleteTextView.setAdapter(arrayAdapter)

            binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
                Toast.makeText(requireContext(), i.toString(), Toast.LENGTH_SHORT).show()
                moneyEntity = cards[i]
                moneyEntityId = i
            }
        })

        binding.today.setOnClickListener {
            sortChoice = 0
            selectedItem(sortChoice!!)
        }

        binding.yesterday.setOnClickListener {
            sortChoice = 1
            selectedItem(sortChoice!!)
        }

        binding.week.setOnClickListener {
            sortChoice = 2
            selectedItem(sortChoice!!)
        }

        binding.month.setOnClickListener {
            sortChoice = 3
            selectedItem(sortChoice!!)
        }

        binding.year.setOnClickListener {
            sortChoice = 4
            selectedItem(sortChoice!!)
        }

        binding.sortButton.setOnClickListener {

            sortViewModel.saveSortCardType(moneyEntityId!!, sortChoice!!)

            val action = SortFragmentDirections.actionSortFragmentToHistoryFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun clickedSort(textView: TextView, imageView: ImageView, flag: Boolean) {
        if (flag) {
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.darkPurple))
            imageView.visibility = View.VISIBLE
        }
        else {
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.lightGray))
            imageView.visibility = View.INVISIBLE
        }
    }

    private fun selectedItem(choice: Int) {
        if (choice == 0)
            clickedSort(binding.todayTv, binding.todayCheck, true)
        else
            clickedSort(binding.todayTv, binding.todayCheck, false)

        if (choice == 1)
            clickedSort(binding.yesterdayTv, binding.yesterdayCheck, true)
        else
            clickedSort(binding.yesterdayTv, binding.yesterdayCheck, false)

        if (choice == 2)
            clickedSort(binding.weekTv, binding.weekCheck, true)
        else
            clickedSort(binding.weekTv, binding.weekCheck, false)

        if (choice == 3)
            clickedSort(binding.monthTv, binding.monthCheck, true)
        else
            clickedSort(binding.monthTv, binding.monthCheck, false)

        if (choice == 4)
            clickedSort(binding.yearTv, binding.yearCheck, true)
        else
            clickedSort(binding.yearTv, binding.yearCheck, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}