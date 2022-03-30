package com.example.arifitna.ui.SignIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.example.arifitna.R
import com.example.arifitna.ui.MainActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(R.layout.activity_sign_in) {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        btIn.setOnClickListener {
            Log.d("Test", "btIN")
            val intent = Intent(this, MainActivity::class.java).apply {
//                putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }
    }
}