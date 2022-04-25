package com.example.arifitna.ui.profile

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.arifitna.R
import com.example.arifitna.extensions.downloadImage
import com.example.arifitna.extensions.observe
import com.example.arifitna.model.UserStorage
import com.example.arifitna.ui.signIn.SignInActivity
import com.example.arifitna.util.Constants.CURRENT_ID
import com.example.arifitna.model.room.dto.PendingInt
import com.example.arifitna.util.Constants.REF_DATABASE
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel by viewModel<ProfileFragmentViewModel>()

    private val _pendingIntList = MutableLiveData<List<PendingInt>>()
    var pendingIntList: LiveData<List<PendingInt>> = _pendingIntList
    lateinit var imageUri : Uri

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
        observe(viewModel.userDataLiveData, ::userDataUpdate)
    }

    private fun userDataUpdate(userStorage: UserStorage?) {
        userStorage?.let {
            profile_name.text = userStorage.name
            profile_balance.text = "Норма воды: " + userStorage.normWater.toString() + "мл"
            settings_user_photo.downloadImage(userStorage.photoUrl)
        }
    }

    private fun setupOnClickListener() {
        moreDetails.setOnClickListener {
            var dial = layoutInflater.inflate(R.layout.dialog_detail_bonus, null)
            MaterialAlertDialogBuilder(requireContext())
                .setView(dial)
                .setPositiveButton(resources.getString(R.string.close)) { dialog, which ->
                    // Respond to negative button press
                    dialog.cancel()
                }
                .show()
        }

        btExit.setOnClickListener {
            sharedPreferencesEditor.apply {
                putBoolean("INIT", false)
                apply()
            }
            viewModel.signOut()
            toSignIn()
        }

        settings_change_photo.setOnClickListener {
            selectImage()
        }

        settings_change_data.setOnClickListener {

        }
    }
    private fun changePhotoUser(){
        val progressDialog  = ProgressDialog(requireContext())
        progressDialog.apply {
            setMessage("Загрузка изображения...")
            setCancelable(false)
            show()
        }

        val storageReference = FirebaseStorage.getInstance().getReference("profilePhoto/$CURRENT_ID")
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnCompleteListener {
                    if(it.isSuccessful){
                        val photoUrl = it.result.toString()
                        REF_DATABASE?.child("users")?.child(CURRENT_ID)?.child("photoUrl")?.setValue(photoUrl)
                            ?.addOnCompleteListener {
                                if(it.isSuccessful){
                                    settings_user_photo.downloadImage(photoUrl)
                                    Toast.makeText(requireContext(), "Фотография изменена", Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    Toast.makeText(requireContext(), "Ошибка при сохранении фотографии", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
                if(progressDialog.isShowing){
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "Ошибка при сохранении фотографии", Toast.LENGTH_SHORT).show()
                if(progressDialog.isShowing){
                    progressDialog.dismiss()
                }
            }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("hhh", "onActivityResult ")
        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            changePhotoUser()
        }
    }

    private fun toSignIn() {
        val intent = Intent(requireContext(), SignInActivity::class.java)
        startActivity(intent)
    }

}