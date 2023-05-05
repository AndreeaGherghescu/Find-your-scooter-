package com.example.trotinete.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.trotinete.CustomAdapter
import com.example.trotinete.R
import com.example.trotinete.RidesRepository
import com.example.trotinete.databinding.FragmentLoginBinding
import com.example.trotinete.databinding.FragmentRidesBinding
import kotlinx.coroutines.launch


class RidesFragment : Fragment() {
    private lateinit var binding: FragmentRidesBinding
    lateinit var customAdapter: CustomAdapter
    val repo = RidesRepository()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRidesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customAdapter = CustomAdapter {  }
        binding.ridesList.adapter = customAdapter

        repo.getAllRidesFromFirebase { ridesList ->
            binding.progressCircular.visibility = View.GONE
            customAdapter.update(ridesList)
        }
    }

}

