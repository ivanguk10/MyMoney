package com.example.mymoney.ui.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColor
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoney.OnCardViewClickListener
import com.example.mymoney.adapters.CardAdapter
import com.example.mymoney.R
import com.example.mymoney.database.entities.MoneyEntity
import com.example.mymoney.databinding.FragmentHomeBinding
import com.example.mymoney.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnCardViewClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val cardAdapter: CardAdapter by lazy { CardAdapter(requireActivity(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

        binding.addNewCardBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNewCardFragment)
        }

        binding.weekStat.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_weekStatisticsFragment)
        }

        binding.monthStat.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_monthStatisticsFragment)
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
        cardAdapter.clearActionMode()
    }

    override fun onCardClick(data: Int) {
        binding.homeRecyclerView.smoothScrollToPosition(data)
    }


}