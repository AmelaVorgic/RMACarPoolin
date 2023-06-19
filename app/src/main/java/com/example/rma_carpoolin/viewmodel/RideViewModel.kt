package com.example.rma_carpoolin.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.rma_carpoolin.model.Ride
import com.example.rma_carpoolin.model.User
import com.example.rma_carpoolin.repository.RideRepository
import com.example.rma_carpoolin.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RideViewModel(
    private val rideRepository: RideRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var firebaseAuth: FirebaseAuth = Firebase.auth
    var rides = rideRepository.getAllRides()
    var users = userRepository.getAllUsers()

    fun saveRide(ride: Ride){
        rideRepository.save(ride)
        rides = rideRepository.getAllRides()
    }

    fun saveUser(user: User){
        val result = userRepository.save(user)
        users = userRepository.getAllUsers()
        return result
    }

    fun updateUser(user: User, picture: Uri){
        userRepository.update(user, picture)
        users = userRepository.getAllUsers()
    }

    fun isUserRegistered(email: String, password: String){
        return userRepository.isRegistered(email, password)
    }

    fun getUserById(): User? {
        return userRepository.getUserById(firebaseAuth.currentUser!!.uid)
    }
}
