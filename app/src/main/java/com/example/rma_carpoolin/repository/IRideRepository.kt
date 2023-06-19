package com.example.rma_carpoolin.repository

import androidx.lifecycle.MutableLiveData
import com.example.rma_carpoolin.model.Ride

interface IRideRepository {
    fun save(ride: Ride)
    fun getRideById(id: String): Ride?
    fun getAllRides(): MutableLiveData<MutableList<Ride>?>
}