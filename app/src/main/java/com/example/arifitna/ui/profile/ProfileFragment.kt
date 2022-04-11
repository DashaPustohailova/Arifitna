package com.example.arifitna.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.arifitna.R
import com.example.arifitna.extensions.observe
import com.example.arifitna.model.UserStorage
import com.example.arifitna.ui.MainActivity
import com.example.arifitna.ui.signIn.SignInActivity
import com.example.focusstart.model.room.dto.PendingInt
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel by viewModel<ProfileFragmentViewModel>()

    private val _pendingIntList = MutableLiveData<List<PendingInt>>()
    var pendingIntList: LiveData<List<PendingInt>> = _pendingIntList

    private val sharedPreferences: SharedPreferences by inject<SharedPreferences>()
    private val sharedPreferencesEditor: SharedPreferences.Editor by inject<SharedPreferences.Editor>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadPendingInt()
        viewModel.updateUserData()
        setupObservers()
        setupOnClickListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupObservers() {
        observe(viewModel.userLiveData, ::userDataUpdate)
    }

    private fun userDataUpdate(userStorage: UserStorage?) {
        userStorage?.let {
            etName.setText("${userStorage.name}")
            etWeight.setText("${userStorage.weight}")
            etNormWater.setText("${userStorage.normWater}")
        }
    }

    private fun setupOnClickListener() {
        btExit.setOnClickListener {
            sharedPreferencesEditor.apply {
                putBoolean("INIT", false)
                apply()
            }
            viewModel.signOut()
            toSignIn()
        }
    }

    private fun toSignIn() {
        val intent = Intent(requireContext(), SignInActivity::class.java)
        startActivity(intent)
    }

}