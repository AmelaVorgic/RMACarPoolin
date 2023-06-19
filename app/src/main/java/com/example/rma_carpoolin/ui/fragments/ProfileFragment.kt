package com.example.rma_carpoolin.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rma_carpoolin.LoginActivity
import com.example.rma_carpoolin.databinding.ProfileFragmentBinding
import com.example.rma_carpoolin.viewmodel.RideViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: ProfileFragmentBinding
    private var firebaseAuth: FirebaseAuth = Firebase.auth

    private val viewModel by viewModel<RideViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProfileFragmentBinding.inflate(layoutInflater)
        binding.imageHolder.setOnClickListener { uploadImage() }
        binding.backButton.setOnClickListener { backToDisplay() }
        binding.logoutButton.setOnClickListener { logout() }

        val handler = Handler()
        handler.postDelayed(Runnable
        {
            run(){
                if(viewModel.getUserById()?.picture != "")
                Picasso.get().load(viewModel.getUserById()?.picture).into(binding.iconProfile)
            }
        },2000)
        return binding.root
    }

    private fun uploadImage() {
        getProfileImage.launch("image/*")
    }

    private val getProfileImage =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){
                picture ->
            if(picture != null){
                binding.iconProfile.setImageURI(picture)
                viewModel.getUserById()
                    ?.let{ user
                        -> viewModel.updateUser(user, picture) }
            }
        }

    private fun backToDisplay() {
        val action = ProfileFragmentDirections.actionProfileFragmentToAllRidesFragment()
        findNavController().navigate(action)
    }

    private fun logout() {
        firebaseAuth.signOut()
        startActivity(Intent(context, LoginActivity::class.java))
    }
}

