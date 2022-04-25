package com.example.arifitna.ui.signIn.signInFragment


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.arifitna.R
import com.example.arifitna.extensions.observe
import com.example.arifitna.ui.MainActivity
import com.example.arifitna.util.Constants.CURRENT_ID
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val sharedPreferences: SharedPreferences by inject<SharedPreferences>()
    private val sharedPreferencesEditor: SharedPreferences.Editor by inject<SharedPreferences.Editor>()
    private val viewModel by viewModel<SignInViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (sharedPreferences.getBoolean("INIT", true)) toLk()
        setupOnClickListener()
        setupObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun toLk() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun setupOnClickListener() {
        btIn.setOnClickListener {
            viewModel.signIn(input_email.text.toString(), input_password.text.toString())
        }

        btRegistrate.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_registrationFragment)
        }
    }

    private fun setupObservers() {
        observe(viewModel.nowFragment, ::nowFragment)
    }

    private fun nowFragment(nowFragment: String?) {
        when (nowFragment) {
            "signIn" -> {

            }
            "lkFragment" -> {
                sharedPreferencesEditor.apply {
                    putBoolean("INIT", true)
                    putString("CURRENT_ID", CURRENT_ID)
                    apply()
                }
                toLk()
            }
            "reqistration" -> {

            }
        }
    }
}
