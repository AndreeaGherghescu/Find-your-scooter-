package com.example.trotinete
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.util.Date

class RidesRepository {

    val database = Firebase.database.reference

    fun getAllRidesFromFirebase(callback: (List<Ride>) -> Unit) {
        val rideList = mutableListOf<Ride>()

        database.child("Rides").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rideList.clear()
                for (rideSnapshot in snapshot.children) {
                    val key = rideSnapshot.key?.toInt() ?: -1
                    val startTime = rideSnapshot.child("startTime").value?.toString() ?: ""
                    val endTime = rideSnapshot.child("endTime").value?.toString() ?: ""

                    var startDate: Date? = null
                    var endDate: Date? = null
                    if (startTime != "") {
                        startDate = getTime(startTime)
                    }
                    if (endTime != "") {
                        endDate = getTime(endTime)
                    }

                    val ride = Ride(key, startDate, endDate)
                    rideList.add(ride)
                }
                callback(rideList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun getTime(str: String): Date {
        val jsonObject = JSONObject(str)
        val day = jsonObject.getInt("date")
        val hours = jsonObject.getInt("hours")
        val minutes = jsonObject.getInt("minutes")
        val month = jsonObject.getInt("month")
        val year = jsonObject.getInt("year")
        return Date(year, month, day, hours, minutes)
    }

}