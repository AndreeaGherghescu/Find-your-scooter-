package com.example.trotinete.Login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trotinete.R
import com.example.trotinete.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, SplashFragment::class.java, null)
            .commit()


    }


}