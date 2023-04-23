package com.example.trotinete.Home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trotinete.InitializeScootersFragment
import com.example.trotinete.MapsActivity
import com.example.trotinete.R
import com.example.trotinete.databinding.FragmentHomeBinding
import com.example.trotinete.databinding.FragmentLoginBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startRide.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)


//      Used this to manualy add some scooter locations in the database. From now on, i'll work on them
//            requireActivity().supportFragmentManager.beginTransaction().apply {
//                add(R.id.container, InitializeScootersFragment::class.java, null)
//                commit()
//            }
        }
    }

}