package com.example.trotinete.Home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.trotinete.Map.MapsActivity
import com.example.trotinete.databinding.FragmentHomeBinding

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
            if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(requireContext(), MapsActivity::class.java)
                startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    HomeActivity.LOCATION_REQUEST_CODE)
            }


//      Used this to manually add some scooter locations in the database. From now on, i'll work with them
//            requireActivity().supportFragmentManager.beginTransaction().apply {
//                add(R.id.container, InitializeScootersFragment::class.java, null)
//                commit()
//            }
        }
    }



}