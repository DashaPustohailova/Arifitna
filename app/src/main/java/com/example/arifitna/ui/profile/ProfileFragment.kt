package com.example.arifitna.ui.profile

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.arifitna.R
import com.example.arifitna.extensions.downloadImage
import com.example.arifitna.extensions.observe
import com.example.arifitna.model.Price
import com.example.arifitna.model.UserPermissions
import com.example.arifitna.model.UserStorage
import com.example.arifitna.ui.signIn.SignInActivity
import com.example.arifitna.util.Constants.CURRENT_ID
import com.example.arifitna.util.Constants.REF_DATABASE
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.dialog_update_personal_data.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel by viewModel<ProfileFragmentViewModel>()

    lateinit var imageUri: Uri

    private val sharedPreferences: SharedPreferences by inject<SharedPreferences>()
    private val sharedPreferencesEditor: SharedPreferences.Editor by inject<SharedPreferences.Editor>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadPendingInt()
        viewModel.updateUserData()
        viewModel.updateUserPermission()
        viewModel.updatePrice()
        setupObservers()
        setupOnClickListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupObservers() {
        observe(viewModel.userDataLiveData, ::userDataUpdate)
        observe(viewModel.message, ::messageUpdate)
        observe(viewModel.userPermission, ::userPermissionUpdate)
        observe(viewModel.price, ::priceUpdate)

    }

    private fun priceUpdate(price: Price?) {
//        Toast.makeText(requireContext(), price?.drinks.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun userPermissionUpdate(userPermissions: UserPermissions?) {
        settings_change_photo.isEnabled = userPermissions?.drinks == true
    }

    fun messageUpdate(s: String?) {
        if(s != "")
            Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    private fun userDataUpdate(userStorage: UserStorage?) {
        userStorage?.let {
            profile_name.text = userStorage.name
            profile_balance.text = "Норма воды: " + userStorage.normWater.toString() + "мл"
            tvBalance.text = "Вам доступно: ${userStorage.bonus} бонусов"
            if (userStorage.photoUrl.isNotEmpty()) settings_user_photo.downloadImage(userStorage.photoUrl)
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
        button.setOnClickListener {
            var dial = layoutInflater.inflate(R.layout.dialog_add_bonus, null)
            dial.apply {
                if(viewModel.userPermission?.value?.updatePhoto == true){
                    findViewById<Button>(R.id.btPhoto).text = "Доступно"
                    findViewById<Button>(R.id.btPhoto).isEnabled = false
                }
                else{
                    findViewById<Button>(R.id.btPhoto).text = "Оплатить ${viewModel.price.value?.updatePhoto} бонусов"
                    findViewById<Button>(R.id.btPhoto).isEnabled = true
                    findViewById<Button>(R.id.btPhoto).setOnClickListener {
                        if(viewModel.price.value?.updatePhoto ?: 100000000 <= viewModel.userDataLiveData.value?.bonus ?: 0){
                            viewModel.pay(viewModel.price.value?.updatePhoto?:0)
                            viewModel.payToPermissionSuccess("updatePhoto")

                        }
                        else{
                            Toast.makeText(requireContext(), "У вас недостаточно бонусов", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                if(viewModel.userPermission?.value?.drinks == true){
                    findViewById<Button>(R.id.btDrinks).text = "Доступно"
                    findViewById<Button>(R.id.btDrinks).isEnabled = false
                }
                else{
                    findViewById<Button>(R.id.btDrinks).text = "Оплатить ${viewModel.price.value?.drinks} бонусов"
                    findViewById<Button>(R.id.btDrinks).isEnabled = true
                    findViewById<Button>(R.id.btDrinks).setOnClickListener {
                        if(viewModel.price.value?.drinks ?: 100000000 <= viewModel.userDataLiveData.value?.bonus ?: 0){
                            viewModel.pay(viewModel.price.value?.drinks?:0)
                            viewModel.payToPermissionSuccess("drinks")
                            findViewById<Button>(R.id.btDrinks).isEnabled = false
                            findViewById<Button>(R.id.btDrinks).text = "Доступно"
                        }
                        else{
                            Toast.makeText(requireContext(), "У вас недостаточно бонусов", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            MaterialAlertDialogBuilder(requireContext())
                .setView(dial)
                .setNegativeButton("Закрыть") { dialog, which ->
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
            var dial = layoutInflater.inflate(R.layout.dialog_update_personal_data, null)
            dial.findViewById<EditText>(R.id.updateName).setText(viewModel.userDataLiveData.value?.name ?: "", TextView.BufferType.EDITABLE)
            dial.findViewById<EditText>(R.id.updateWeight).setText(viewModel.userDataLiveData.value?.weight.toString() ?: "0" , TextView.BufferType.EDITABLE)
            MaterialAlertDialogBuilder(requireContext())
                .setView(dial)
                .setPositiveButton("Сохранить") { dialog, which ->
                    // Respond to negative button press
                    viewModel.updatePersonalData(dial.findViewById<EditText>(R.id.updateName).getText().toString(), dial.findViewById<EditText>(R.id.updateWeight).getText().toString())
                    dialog.cancel()
                }
                .show()
        }
    }

    private fun changePhotoUser() {
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.apply {
            setMessage("Загрузка изображения...")
            setCancelable(false)
            show()
        }

        val storageReference =
            FirebaseStorage.getInstance().getReference("profilePhoto/$CURRENT_ID")
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val photoUrl = it.result.toString()
                        REF_DATABASE?.child("users")?.child(CURRENT_ID)?.child("photoUrl")
                            ?.setValue(photoUrl)
                            ?.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    settings_user_photo.downloadImage(photoUrl)
                                    Toast.makeText(
                                        requireContext(),
                                        "Фотография изменена",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Ошибка при сохранении фотографии",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Ошибка при сохранении фотографии",
                    Toast.LENGTH_SHORT
                ).show()
                if (progressDialog.isShowing) {
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
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            changePhotoUser()
        }
    }

    private fun toSignIn() {
        val intent = Intent(requireContext(), SignInActivity::class.java)
        startActivity(intent)
    }

}