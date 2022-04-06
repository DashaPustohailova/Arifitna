package com.example.arifitna.ui.signIn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.arifitna.R

class SignInActivity : AppCompatActivity(R.layout.activity_sign_in) {

    private lateinit var mNavControllerSignIn: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigationGraph()
    }


    private fun initNavigationGraph() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        mNavControllerSignIn = navHostFragment.navController
    }

    private fun setOnClickListener() {

    }
}