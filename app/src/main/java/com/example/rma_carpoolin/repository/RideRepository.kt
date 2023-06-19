package com.example.rma_carpoolin.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.rma_carpoolin.model.Ride
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RideRepository : IRideRepository {
    private val firebaseStore: FirebaseFirestore = Firebase.firestore
    private val collectionReference: CollectionReference = firebaseStore.collection("rides")

    private val liveData: MutableLiveData<MutableList<Ride>?> = MutableLiveData()
    private var isDataFetched = false

    val documentRef = collectionReference.document()

    override fun save(ride: Ride) {
        documentRef
        val newride: MutableMap<String, Any> = HashMap()
        newride["id"] = ride.id
        newride["departure"] = ride.departure
        newride["arrival"] = ride.arrival
        newride["seats"] = ride.seats
        newride["date"] = ride.date
        newride["number"] = ride.number

        documentRef.set(newride)
            .addOnSuccessListener {
                val updated: MutableList<Ride>? = liveData.value
                updated?.add(0, ride)
                liveData.postValue(updated)
            }
            .addOnFailureListener { exception ->
            }
    }

    override fun getRideById(id: String): Ride? {
        val rides = liveData.value?.iterator()
        rides?.forEach {
            if (it.id == id) {
                return it
            }
        }
        return null
    }

    override fun getAllRides(): MutableLiveData<MutableList<Ride>?> {
        if (!isDataFetched) {
            collectionReference.get().addOnSuccessListener { snapshots ->
                if (snapshots != null && !snapshots.isEmpty) {
                    val rides = mutableListOf<Ride>()

                    for (ride in snapshots) {
                        rides.add(
                            Ride(
                                ride.getString("id") ?: "",
                                ride.getString("departure") ?: "",
                                ride.getString("arrival") ?: "",
                                ride.getString("seats") ?: "",
                                ride.getString("date") ?: "",
                                ride.getString("number") ?: ""
                            )
                        )
                    }
                    liveData.postValue(rides)
                } else {
                    Log.d(ContentValues.TAG, "No data")
                }
            }
            isDataFetched = true
        }
        return liveData
    }

}
