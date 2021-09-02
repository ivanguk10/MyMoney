package com.example.mymoney.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.mymoney.R
import com.example.mymoney.databinding.FragmentFirstScreenBinding
import com.example.mymoney.databinding.FragmentSecondScreenBinding


class FirstScreen : Fragment() {

    private var _binding: FragmentFirstScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstScreenBinding.inflate(layoutInflater, container, false)
        //binding.lifecycleOwner = viewLifecycleOwner

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.nextTextView.setOnClickListener {
            viewPager?.currentItem = 1
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}