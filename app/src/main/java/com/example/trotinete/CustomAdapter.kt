package com.example.trotinete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trotinete.databinding.RideRowLayoutBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class CustomAdapter(
    private val onItemClick: (Ride) -> Unit
): RecyclerView.Adapter<CustomAdapter.RidesHolder>() {

    lateinit var databaseReference: DatabaseReference
    private val calendar = Calendar.getInstance()
    lateinit var binding: RideRowLayoutBinding
    val listItems = mutableListOf<Ride>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RidesHolder {
        binding = RideRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RidesHolder(binding)
    }

    override fun onBindViewHolder(holder: RidesHolder, position: Int) {
        val item = listItems[position]
        holder.key.text = item.key.toString()
        holder.start.text = "Start time: ".plus(item.startTime.toString())

        val ft = item.finishTime.toString()
        if (ft == "null") {
            holder.finish.text = "Finish time: -"
        } else {
            holder.finish.text = "Finish time: ".plus(ft)
        }

        holder.button.setOnClickListener {
            databaseReference = Firebase.database.reference

            var tableReference = databaseReference.child("Scooters")
            val childUpdates = mapOf<String, Any>("visibility" to true)
            tableReference.child(item.key.toString()).updateChildren(childUpdates)

            tableReference = databaseReference.child("Rides")
            tableReference.child(item.key.toString()).removeValue()

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