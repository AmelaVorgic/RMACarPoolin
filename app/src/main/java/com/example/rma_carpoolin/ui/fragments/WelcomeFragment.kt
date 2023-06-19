package com.example.rma_carpoolin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rma_carpoolin.databinding.WelcomeFragmentBinding

class WelcomeFragment: Fragment() {

    private lateinit var binding: WelcomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WelcomeFragmentBinding.inflate(layoutInflater)
        binding.loginButton.setOnClickListener { login() }
        binding.registerButton.setOnClickListener { register() }
        return binding.root
    }

    private fun register() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment()
        findNavController().navigate(action)

    }

    private fun login() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
        findNavController().navigate(action)
    }
}