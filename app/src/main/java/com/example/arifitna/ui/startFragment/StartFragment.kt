package com.example.arifitna.ui.startFragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import com.example.arifitna.R
import com.example.arifitna.extensions.observe
import com.example.arifitna.model.Report
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
            }
        }
        viewModel.updateCurrentReport()
        viewModel.updateLastReport()
        setupObservers()
        setupOnClickListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupObservers() {
        observe(viewModel.currentReport, ::currentReportObserve)
        observe(viewModel.lastReport, ::lastReportObserve)
    }

    private fun lastReportObserve(report: Report?) {
        report?.let {
            tvDate.text = "${report.date}"
            tvWater.setText("${report.water} мл")
        }
    }

    private fun currentReportObserve(count: Long?) {
        count?.let{
            Log.d("Test", count.toString())
            viewModel.createReport(count)
        }
    }

    private fun initBaseData(current_id: String) {
        viewModel.initBaseData(current_id)
    }

    private fun setupOnClickListener() {
        btEnter.setOnClickListener {
        }
    }
}