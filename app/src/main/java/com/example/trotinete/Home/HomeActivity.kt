package com.example.trotinete.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.trotinete.R
import com.example.trotinete.databinding.ActivityHomeBinding
import com.example.trotinete.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNav: BottomNavigationView

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
}