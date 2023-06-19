package com.example.rma_carpoolin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rma_carpoolin.databinding.AddrideFragmentBinding
import com.example.rma_carpoolin.model.Ride
import com.example.rma_carpoolin.viewmodel.RideViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddRideFragment : Fragment(){

    private lateinit var binding: AddrideFragmentBinding
    private val viewModel by viewModel<RideViewModel>()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddrideFragmentBinding.inflate(layoutInflater)
        addingNewRide()
        binding.backButton.setOnClickListener { backToDisplay() }

        return binding.root
    }

    private fun addingNewRide() {
        binding.addButton.setOnClickListener {
            val collectionRef = firestore.collection("rides")
            val id: String = (0..1000).random().toString()
            val departure = binding.departureHolder.text.toString()
            val arrival = binding.arrivalHolder.text.toString()
            val seats = binding.seatsHolder.text.toString()
            val date = binding.dateHolder.text.toString()
            val number = binding.rideNumberHolder.text.toString()

            viewModel.saveRide(Ride(
                id,
                departure,
                arrival,
                seats,
                date,
                number
            ))
            backToDisplay()
        }
    }


    private fun backToDisplay() {
        val action = AddRideFragmentDirections.actionAddRideFragmentToAllRidesFragment()
        findNavController().navigate(action)
    }

}