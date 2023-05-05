package com.example.trotinete.Map

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.trotinete.R
import com.example.trotinete.Ride
import com.example.trotinete.Scooter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.trotinete.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {

    internal lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (10 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private lateinit var database: DatabaseReference
    private lateinit var scootersReference: DatabaseReference
    private val scooterMarkers = hashMapOf<Scooter, Marker>()
    private lateinit var scooterKeys: Set<String>
    private var userMarker: Marker? = null
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        database = Firebase.database.reference
        scootersReference = database.child("Scooters")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLong = LatLng(location.latitude, location.longitude)
                    placeMarkerOnMap(currentLatLong)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
                    positionScooters()
                    startLocationUpdates()
                }
            }
        }
    }

    private fun positionScooters() {
        // retrieve every scooter from db, then add it on map
        val scootersListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                scooterKeys = snapshot.children.map {it.key!!}.toSet()
                val iterator = scooterMarkers.iterator()
                while (iterator.hasNext()) {
                    val (scooter, marker) = iterator.next()
                    if (scooter.key.toString() !in scooterKeys) {
                        marker.remove()
                        iterator.remove()
                    }
                }

                for (child in snapshot.children) {
                    val scooter = getScooterFromString(child.value.toString())
                    val marker = scooterMarkers[scooter]

                    if (marker == null) {
                        // Add a new marker for the scooter
                        val latLng = LatLng(scooter.latitude, scooter.longitude)
                        val markerOptions = MarkerOptions().position(latLng)
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        markerOptions.visible(scooter.visibility)

                        val newMarker : Marker = mMap.addMarker(markerOptions)!!
                        newMarker.tag = scooter.key

                        mMap.setOnMarkerClickListener { m ->
                            addRideToDB(m.tag as Int)
                            m.isVisible = false
                            val childUpdates = mapOf<String, Any>("visibility" to "false")
                            scootersReference.child(m.tag.toString()).updateChildren(childUpdates)

                            true
                        }

                        scooterMarkers[scooter] = newMarker

                    } else {
                        // Update the position of the existing marker
                        marker.position = LatLng(scooter.latitude, scooter.longitude)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MapsActivity, "Failed to get data.", Toast.LENGTH_SHORT).show();
            }
        }
        scootersReference.addValueEventListener(scootersListener)

    }

    private fun getScooterFromString(str : String): Scooter {

        val regex = Regex("""\{visibility=(true|false), latitude=([\d.]+), key=(\d+), longitude=([\d.]+)\}""")
        val matchResult = regex.find(str)

        val visibility = matchResult!!.groupValues[1].toBoolean()
        val latitude = matchResult.groupValues[2].toDouble()
        val key = matchResult.groupValues[3].toInt()
        val longitude = matchResult.groupValues[4].toDouble()

        return Scooter(key, latitude, longitude, visibility)
    }

    private fun addRideToDB(key: Int) {
        val ride = Ride(key, calendar.time, null)
        database.child("Rides").child(key.toString()).setValue(ride).addOnSuccessListener {
            Toast.makeText(this, "Ride started!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Couldn't start ride", Toast.LENGTH_SHORT).show()
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        userMarker = mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker) = false

    private fun startLocationUpdates() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.run {
            setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            setInterval(UPDATE_INTERVAL)
            setFastestInterval(FASTEST_INTERVAL)
        }

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        registerLocationListener()
    }

    private fun registerLocationListener() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation = locationResult.lastLocation ?: return
                onLocationChanged(lastLocation)
            }
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            mLocationRequest?.let { request ->
                locationCallback?.let { callback ->
                    fusedLocationClient.requestLocationUpdates(request, callback, Looper.myLooper())
                }
            }
        }
    }

    private fun onLocationChanged(location: Location) {
        val location = LatLng(location.latitude, location.longitude)
        userMarker?.position = location
    }
}