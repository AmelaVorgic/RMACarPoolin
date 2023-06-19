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
import com.example.rma_carpoolin.databinding.LoginFragmentBinding
import com.example.rma_carpoolin.viewmodel.RideViewModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment: Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private var handler: Handler = Handler()
    private val viewModel by viewModel<RideViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(layoutInflater)
        binding.loginButton.setOnClickListener { login() }
        binding.backButton.setOnClickListener { back() }
        return binding.root
    }

    private fun back() {

        val action = LoginFragmentDirections.actionLoginFragmentToWelcomeFragment()
        findNavController().navigate(action)
    }


    private fun login() {
        val email = binding.emailET.text.toString().trim()
        val password = binding.passwordET.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            binding.emailET.setError("Please enter your email.")
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordET.setError("Please enter your password")
        } else {
            viewModel.isUserRegistered(email, password)
            handler.postDelayed(Runnable {
                run(){
                    if(firebaseAuth.currentUser != null){
                        startActivity(
                            Intent(
                                context,
                                    MainActivity::class.java
                            )
                        )
                    }else
                        Toast.makeText(context, "Please try again!", Toast.LENGTH_LONG).show()
                }
            }, 3000)
        }
    }
}