package com.example.trotinete

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class InitializeScootersFragment : Fragment() {

    private lateinit var database: DatabaseReference
    var scooterIds = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scooters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locations = arrayOf<LatLng>(LatLng(44.399751, 26.112224),
            LatLng(44.399383, 26.099006),
            LatLng(44.414099, 26.126043),
            LatLng(44.420230, 26.103899),
            LatLng(44.409072, 26.096002)
            )

        database = Firebase.database.reference

        for (i in 0..locations.size - 1) {

            val location = locations[i]
            val scooter = Scooter(location.latitude, location.longitude)

            database.child("Scooters").child(scooterIds.toString()).setValue(scooter).addOnSuccessListener {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
            scooterIds += 1
        }

    }

}