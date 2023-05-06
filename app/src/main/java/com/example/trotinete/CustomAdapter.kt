package com.example.trotinete

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trotinete.Home.RidesFragment
import com.example.trotinete.Map.MapsActivity
import com.example.trotinete.databinding.RideRowLayoutBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CustomAdapter(
    private val context: Context,
    private val onItemClick: (Ride) -> Unit
): RecyclerView.Adapter<CustomAdapter.RidesHolder>() {

    lateinit var databaseReference: DatabaseReference
    lateinit var binding: RideRowLayoutBinding
    private val listItems = mutableListOf<Ride>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RidesHolder {
        binding = RideRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RidesHolder(binding)
    }

    override fun onBindViewHolder(holder: RidesHolder, position: Int) {
        val item = listItems[position]
        holder.key.text = item.key.toString()
        holder.start.text = "Start time: ".plus(item.startTime.toString())
        holder.finish.text = "Finish until: ".plus(item.finishTime.toString())

        holder.button.setOnClickListener {
            databaseReference = Firebase.database.reference

            val scootersReference = databaseReference.child("Scooters")
            val ridesReference = databaseReference.child("Rides")

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location ->
                        val childUpdates = mapOf<String, Any>("visibility" to true,
                            "latitude" to location.latitude, "longitude" to location.longitude)
                        scootersReference.child(item.key.toString()).updateChildren(childUpdates)
                    }
                    .addOnFailureListener {
                        val childUpdates = mapOf<String, Any>("visibility" to true)
                        scootersReference.child(item.key.toString()).updateChildren(childUpdates)
                    }
            }

            ridesReference.child(item.key.toString()).removeValue()

            notifyItemChanged(item.key)
        }

        holder.constraint.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun update(list: List<Ride>) {
        listItems.clear()
        listItems.addAll(list)
        notifyDataSetChanged()
    }

    class RidesHolder(binding: RideRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        var key = binding.rideKey
        var start = binding.start
        var finish = binding.finish
        var button = binding.stopRide
        var constraint = binding.constraint
    }
}