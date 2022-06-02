package com.example.arifitna.ui.startFragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.arifitna.R
import com.example.arifitna.extensions.observe
import com.example.arifitna.model.Drinks
import com.example.arifitna.model.Report
import com.example.arifitna.model.UserPermissions
import com.example.arifitna.model.UserStorage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_start.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartFragment : Fragment(R.layout.fragment_start) {
    private val viewModel by viewModel<StartFragmentViewModel>()
    private val sharedPreferences: SharedPreferences by inject<SharedPreferences>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var current_id = sharedPreferences.getString("CURRENT_ID", "")

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        current_id?.let {
            if (current_id != "") {
                initBaseData(current_id)
                viewModel.updateUserData()
                viewModel.updateCurrentReport()
                viewModel.updateLastReport()
                viewModel.updateListOfDrinks()
                viewModel.updateUserPermission()
            }
        }
        setupObservers()
        setupOnClickListener()
        setupLocation()
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    private fun setupLocation() {
        if (checkPermissions()) {

            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location: Location? = it.result
                    if (location == null || location.accuracy > 100) {
//                        Toast.makeText(requireContext(), "location is null", Toast.LENGTH_SHORT)
//                            .show()

                    } else {
                        Log.d("weather", "" + location.latitude + "," + location.longitude)
                        getResultWeather(location.latitude, location.longitude)
//                        tvWeather.text = ""+location.latitude + "," + location.longitude
                    }
                }
            } else {
                //settings open here
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            //request permission here

            Toast.makeText(requireContext(), "else requirePermisiion()", Toast.LENGTH_SHORT).show()
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            (requireContext().getSystemService(LOCATION_SERVICE) as LocationManager)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun checkPermissions(): Boolean =
        (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)


    private fun setupObservers() {
        observe(viewModel.currentReport, ::currentReportObserve)
        observe(viewModel.lastReport, ::lastReportObserve)
        observe(viewModel.userDataLiveData, ::userDataObserve)
        observe(viewModel.percent, ::persentUpdate)
        observe(viewModel.listDrinks, ::updateListDrinks)
        observe(viewModel.userPermission, ::userPermissionUpdate)
    }

    private fun userPermissionUpdate(userPermissions: UserPermissions?) {
//        Toast.makeText(requireContext(), userPermissions?.drinks.toString(), Toast.LENGTH_SHORT).show()
        btAddDrinks.isEnabled = userPermissions?.drinks == true
    }
    private fun updateListDrinks(list: List<Drinks>?) {

    }


    private fun persentUpdate(s: Int?) {
        tvProgressBar.text = s.toString() + "%" ?: ""
        progress_bar.progress = s ?: 0
    }

    private fun userDataObserve(userStorage: UserStorage?) {
        userStorage?.let {
            tvProgressBarDetail2.text = userStorage.normWater.toString() + "мл"
            viewModel.normWater = userStorage.normWater.toDouble()
            viewModel.bonus = userStorage.bonus
            viewModel.lastPersent()
        }
    }

    private fun lastReportObserve(report: Report?) {
        report?.let {
            tvProgressBarDetail.text = "${report.water}мл / "
            viewModel.partWater = if (report.water == "") 0.0 else report.water.toDouble()
            tvDate.text = report.date
            viewModel.lastPersent()
        }
    }

    private fun currentReportObserve(count: Long?) {
        count?.let {
            viewModel.createReport(count)
            viewModel.lastPersent()
        }
    }

    private fun initBaseData(current_id: String) {
        viewModel.initBaseData(current_id)
    }

    private fun setupOnClickListener() {
        viewModel.updateUserData()
        btEnter.setOnClickListener {
            var dial = layoutInflater.inflate(R.layout.dialog_update_count_water, null)
            MaterialAlertDialogBuilder(requireContext())
                .setView(dial)
                .setNegativeButton(resources.getString(R.string.close)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Добавить") { dialog, which ->
                    viewModel.changeCountWater(
                        dial.findViewById<EditText>(R.id.plusCount).getText().toString()
                    )
                    dialog.dismiss()
                    dialog.cancel()
                }
                .show()
        }

        btAddDrinks.setOnClickListener {
            var dial = layoutInflater.inflate(R.layout.drinks, null)
            var mRecyclerView: RecyclerView = dial.findViewById(R.id.recycler_view_drinks)
            var mAdapter = DrinksAdapter { countWater -> onClickDrinks(countWater) }

            mRecyclerView.adapter = mAdapter
            mAdapter.setList(viewModel.listDrinks.value ?: emptyList())

            MaterialAlertDialogBuilder(requireContext())
                .setView(dial)
                .show()
        }
    }

    fun onClickDrinks(count: Int) {
        viewModel.changeCountWater(count.toString())
        Toast.makeText(requireContext(), "Добавлено ${count}мл воды", Toast.LENGTH_SHORT).show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupLocation()
//                Toast.makeText(requireContext(), "onRequestPermissionsResult", Toast.LENGTH_SHORT)
//                    .show()

            }
        }
    }


    fun getResultWeather(latitude: Double, longitude: Double) {
        val url = "https://api.weatherapi.com/v1/current.json" +
                "?key=$API_KEY" +
                "&q=$latitude,$longitude" +
                "&aqi=no"
        val queue = Volley.newRequestQueue(requireContext())
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val obj = JSONObject(response)
                val temp = obj.getJSONObject("current").getString("temp_c")
                tvWeather.text = temp+"℃"
                //лучше поставить когда температура выше 30
                if(temp.toDouble() > 20.0){
                    Toast.makeText(requireContext(), "Сегодня рекомендуется пить больше, чем ваша суточная норма воды", Toast.LENGTH_LONG).show()
                }
            },
            {
                //тут можно выводить сообщение об ошибке
            }
        )
        queue.add(stringRequest)
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
        private const val API_KEY = "6deb57a162514a91bb3230947221805"
    }
}