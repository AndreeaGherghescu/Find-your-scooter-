package com.example.trotinete.Home

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.trotinete.Login.MainActivity
import com.example.trotinete.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var signOut: Button
    private lateinit var gSignInOptions: GoogleSignInOptions
    private lateinit var gSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = binding.name
        email = binding.email
        signOut = binding.signOut
        firebaseAuth = FirebaseAuth.getInstance()
        gSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gSignInClient = GoogleSignIn.getClient(requireContext(), gSignInOptions)

        val googleAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())
        val firebaseAccount = firebaseAuth.currentUser

        if (googleAccount != null) {
            val getName = googleAccount.displayName
            name.text = "Hello, $getName!"
            email.text = googleAccount.email

        }

        if (firebaseAccount != null) {
            name.text = firebaseAccount.displayName
            email.text = firebaseAccount.email
        }

        signOut.setOnClickListener {
            if(googleAccount != null) {
                gSignInClient.signOut().addOnSuccessListener {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
            if (firebaseAccount != null) {
                firebaseAuth.signOut()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }

        }

        val tY = ObjectAnimator.ofFloat(binding.scooterAnimation, "rotation", 0f, 360f)
        tY.duration = 1000
        tY.start()
    }




}