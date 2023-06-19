package com.example.rma_carpoolin.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rma_carpoolin.MainActivity
import com.example.rma_carpoolin.databinding.RegisterFragmentBinding
import com.example.rma_carpoolin.model.User
import com.example.rma_carpoolin.viewmodel.RideViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : Fragment() {
    private lateinit var binding: RegisterFragmentBinding
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private val handler: Handler = Handler()

    private val viewModel by viewModel<RideViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(layoutInflater)
        binding.registerButton.setOnClickListener { moveToApplication() }
        binding.backButton.setOnClickListener { back() }
        return binding.root
    }

    private fun moveToApplication() {
        val email = binding.emailET.text.toString()
        val password = binding.passwordET.text.toString()

        if (TextUtils.isEmpty(email)) {
            binding.emailET.setError("Missing username!")
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordET.setError("Missing password!")
        }
        viewModel.saveUser(
                    User(
                        id = (0..1000).random().toString(),
                        email,
                        password
                    )
                )
                handler.postDelayed(Runnable
                {
                    run() {
                        if (firebaseAuth.currentUser != null) {
                            Toast.makeText(context, "WELCOME!", Toast.LENGTH_LONG).show()
                            startActivity(
                                Intent(
                                    context,
                                    MainActivity::class.java
                                )
                            )
                        } else {
                            Toast.makeText(context, "Could not register user. Please try again.", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }, 3000
                )

            }


    private fun back() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToWelcomeFragment()
        findNavController().navigate(action)
    }
}