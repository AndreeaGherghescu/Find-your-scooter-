package com.example.trotinete.Home

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.trotinete.Map.MapsActivity
import com.example.trotinete.R
import com.example.trotinete.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNav: BottomNavigationView

    companion object {
        const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentFragment(HomeFragment())

        bottomNav = binding.bottomNav
        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.wallet -> {
                    setCurrentFragment(WalletFragment())
                    true
                }
                R.id.rides -> {
                    setCurrentFragment(RidesFragment())
                    true
                }
                R.id.profile -> {
                    setCurrentFragment(ProfileFragment())
                    true
                }
                R.id.home -> {
                    setCurrentFragment(HomeFragment())
                    true
                }
                else -> throw AssertionError()
            }
        }

    }

    private fun setCurrentFragment (fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    // Call the function that requires location permission
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                } else {
                    // Permission denied
                    // Handle the case where the user denies the permission request
                    // You can show a message to the user or disable the functionality that requires the permission
                }
            }
        }
    }
}