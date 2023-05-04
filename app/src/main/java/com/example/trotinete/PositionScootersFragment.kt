package com.example.trotinete

import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class PositionScootersFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var mMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_position_scooters, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference
        val scootersReference = database.child("Scooters")

        // retrieve every scooter from db, then add it on map
        val scootersListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.getValue<Scooter>()
                if (post != null) {
                    placeScooterMarkerOnMap(post)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }
        }
        scootersReference.addValueEventListener(scootersListener)

    }

    private fun placeScooterMarkerOnMap(scooter : Scooter) {
        val latlng = LatLng(scooter.latitude, scooter.longitude)
        val markerOptions = MarkerOptions().position(latlng)
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_scooters_marker_round)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        mMap.addMarker(markerOptions)
    }

}