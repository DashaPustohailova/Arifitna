package com.example.arifitna.ui.startFragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import com.example.arifitna.R
import com.example.arifitna.extensions.observe
import com.example.arifitna.model.Report
import com.example.arifitna.model.UserStorage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_start.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartFragment : Fragment(R.layout.fragment_start) {
    private val viewModel by viewModel<StartFragmentViewModel>()
    private val sharedPreferences: SharedPreferences by inject<SharedPreferences>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var current_id = sharedPreferences.getString("CURRENT_ID", "")
        current_id?.let {
            if (current_id != "") {
                initBaseData(current_id)
                viewModel.updateUserData()
                viewModel.updateCurrentReport()
                viewModel.updateLastReport()
            }
        }
        setupObservers()
        setupOnClickListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupObservers() {
        observe(viewModel.currentReport, ::currentReportObserve)
        observe(viewModel.lastReport, ::lastReportObserve)
        observe(viewModel.userDataLiveData, ::userDataObserve)
        observe(viewModel.percent, ::persentUpdate)
    }

    private fun persentUpdate(s: Int?) {
        tvProgressBar.text = s.toString() + "%" ?: ""
        progress_bar.progress = s ?: 0

    }

    private fun userDataObserve(userStorage: UserStorage?) {
        userStorage?.let {
            tvProgressBarDetail2.text = userStorage.normWater.toString() + "мл"
            viewModel.normWater = userStorage.normWater.toDouble()
        }
    }

    private fun lastReportObserve(report: Report?) {
        report?.let {
            tvProgressBarDetail.text = "${report.water}мл / "
            viewModel.partWater = report.water.toDouble()
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
    }


}