package com.example.arifitna.ui.signIn.registrationFragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.service.autofill.UserData
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.arifitna.R
import com.example.arifitna.extensions.observe
import com.example.arifitna.model.UserStorage
import com.example.arifitna.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.btRegistrate
import kotlinx.android.synthetic.main.fragment_sign_in.input_email
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private val sharedPreferences: SharedPreferences by inject<SharedPreferences>()
    private val sharedPreferencesEditor: SharedPreferences.Editor by inject<SharedPreferences.Editor>()
    private val viewModel by viewModel<RegistrationFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupOnClickListener()
        setupObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupObservers() {
        observe(viewModel.registrationResult, ::registrationResult)
    }

    private fun registrationResult(result: String?) {
        when(result){
            "success" -> {
                sharedPreferencesEditor.apply {
                    putBoolean("INIT", true)
                    apply()
                }
                toLk()
            }
            "fail" -> {
                Toast.makeText(requireContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toLk() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun setupOnClickListener() {
        btRegistrate.setOnClickListener {

            val email = input_email.text.toString()
            val password = etPassword.text.toString()
            val secondPassword = etPassword2.text.toString()
            val name = etName.text.toString()
            val weight = etWeight2.text.toString()
            if(!email.isNullOrEmpty() && !password.isNullOrEmpty()
                && !secondPassword.isNullOrEmpty() && !name.isNullOrEmpty()
                && weight != null) {
                viewModel.registration(
                    inputEmail = email,
                    inputPassword = password,
                    secondPassword = secondPassword,
                    userData = UserStorage(
                        name = name,
                        weight = weight.toInt(),
                        gender = "Woman"
                    )
                )
            }
            else{
                Toast.makeText(requireContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show()
            }
        }
    }
}