package com.example.trotinete.Home

import android.Manifest
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.trotinete.Login.MainActivity
import com.example.trotinete.R
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
    private var imageUri: Uri? = null
    private var imageView: ImageView? = null
    val CAMERA_PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001

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
        imageView = binding.imageView
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

        binding.addPicture.setOnClickListener {
            val permissionGranted = requestCameraPermission()
            if (permissionGranted) {
                openCameraInterface()
            }
        }
    }

    private fun requestCameraPermission(): Boolean {
        var permissionGranted = false
        val cameraPermissionNotGranted = checkSelfPermission(activity as Context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
        if (cameraPermissionNotGranted){
            val permission = arrayOf(Manifest.permission.CAMERA)
            // Display permission dialog
            requestPermissions(permission, CAMERA_PERMISSION_CODE)
        }
        else{
            // Permission already granted
            permissionGranted = true
        }

        return permissionGranted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === CAMERA_PERMISSION_CODE) {
            if (grantResults.size === 1 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                openCameraInterface()
            } else {
                showAlert("Camera permission denied")
            }
        }
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(activity as Context)
        builder.setMessage(message)
        builder.setPositiveButton("Ok", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun openCameraInterface() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Add profile photo")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Your picture")
        imageUri = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        startActivityForResult(intent, IMAGE_CAPTURE_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            imageView?.setImageURI(imageUri)
        } else {
            showAlert("Failed to take picture")
        }
    }




}