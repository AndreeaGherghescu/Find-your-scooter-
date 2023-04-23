package com.example.trotinete.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trotinete.R
import com.example.trotinete.databinding.FragmentLoginBinding
import com.example.trotinete.databinding.FragmentRidesBinding


class RidesFragment : Fragment() {
    private lateinit var binding: FragmentRidesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRidesBinding.inflate(inflater, container, false)
        return binding.root
    }


}