package com.example.rma_carpoolin.ui.fragments

import DisplayRideAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rma_carpoolin.LoginActivity
import com.example.rma_carpoolin.MapsActivity
import com.example.rma_carpoolin.R
import com.example.rma_carpoolin.databinding.AllridesFragmentBinding
import com.example.rma_carpoolin.model.Ride
import com.example.rma_carpoolin.viewmodel.RideViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllRidesFragment : Fragment() {
    private lateinit var binding: AllridesFragmentBinding
    private val firebaseAuth: FirebaseAuth = Firebase.auth
    private val viewModel by viewModel<RideViewModel>()
    private val handler: Handler = Handler()
    private lateinit var adapter: DisplayRideAdapter
    private lateinit var filteredList: MutableList<Ride>

    private companion object {
        const val RADIO_ARRIVAL = R.id.radioArrival
        const val RADIO_DEPARTURE = R.id.radioDeparture
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllridesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DisplayRideAdapter()
        binding.queryResultsRecylcer.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        binding.queryResultsRecylcer.adapter = adapter

        filteredList = mutableListOf()

        viewModel.rides.observe(viewLifecycleOwner) { rides ->
            if (rides != null) {
                filteredList.clear()
                filteredList.addAll(rides)
                adapter.setRides(filteredList)
            }
        }

        binding.profileButton.setOnClickListener { switchToProfile() }
        binding.addButton.setOnClickListener { switchToAddRide() }
        binding.map.setOnClickListener { switchToMap() }
        binding.backButton.setOnClickListener { logout() }

        val searchView = binding.searchEngine
        val radioGroup = binding.searchOptions

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val searchByArrival = (checkedId == RADIO_ARRIVAL)
            adapter.setSearchByArrival(searchByArrival)
        }
    }

    private fun logout() {
        firebaseAuth.signOut()
        startActivity(Intent(context, LoginActivity::class.java))
    }

    private fun switchToMap() {
        startActivity(Intent(context, MapsActivity::class.java))
    }

    private fun switchToAddRide() {
        val action = AllRidesFragmentDirections.actionAllRidesFragmentToAddRideFragment()
        findNavController().navigate(action)
    }

    private fun switchToProfile() {
        val action = AllRidesFragmentDirections.actionAllRidesFragmentToProfileFragment()
        findNavController().navigate(action)
    }
}
