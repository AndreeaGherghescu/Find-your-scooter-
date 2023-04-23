package com.example.trotinete.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trotinete.R
import com.example.trotinete.databinding.FragmentLoginBinding
import com.example.trotinete.databinding.FragmentWalletBinding


class WalletFragment : Fragment() {
    private lateinit var binding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }


}