package com.example.arifitna.ui.statistics

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.arifitna.R
import com.example.arifitna.extensions.observe
import com.example.arifitna.model.Report
import com.example.arifitna.model.UserStorage
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random


class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel by viewModel<StatisticsViewModel>()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: StatisticsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupBottomNavigation()
        viewModel.updateReports()
        viewModel.updateUserData()
        setupRv()
        setupObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupBottomNavigation() {
        bottom_navigation.selectedItemId = R.id.page_1
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    recycler_view.visibility = View.VISIBLE
                    constraintGraphicStatistic.visibility = View.GONE
                    true
                }
                R.id.page_2 -> {
                    recycler_view.visibility = View.GONE
                    constraintGraphicStatistic.visibility = View.VISIBLE
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun setupRv() {
        mAdapter = StatisticsAdapter()
        mRecyclerView = recycler_view
        mRecyclerView.adapter = mAdapter
    }

    private fun persentUpdate1(s: Int?) {
        Log.d("count1", s.toString())
        tvProgressBar1.text = s.toString() + "%"
        progress_bar1.progress = s ?: 0
        Log.d("count1", s.toString())
    }

    private fun persentUpdate2(s: Int?) {
        tvProgressBar2.text = s.toString() + "%"
        progress_bar2.progress = s ?: 0
    }


    private fun setupObservers() {
        observe(viewModel.allReport, ::allReportChange)
        observe(viewModel.userData, ::userDataChange)
        observe(viewModel.percentDay, ::countAllWaterDayUpdate)
        observe(viewModel.sredWater, ::sredWaterUpdate)
        observe(viewModel.sredPercent, ::sredPersentUpdate)
    }

    private fun sredPersentUpdate(i: Int?) {
        persentUpdate2(i?:0)
    }

    private fun sredWaterUpdate(i: Int?) {
        item_date2.text = viewModel.sredWater.value?.toInt().toString() + "мл"
        item_count_water2.text = "среднее количество воды в день"
        persentUpdate2(i?:0)
    }

    private fun countAllWaterDayUpdate(i: Int?) {
        item_date1.text = viewModel.count.toInt().toString() + " из " + viewModel.countDay.toInt().toString()
        item_count_water1.text = "столько дней была выпита норма воды"
        persentUpdate1(i?:0)
    }


    private fun userDataChange(userStorage: UserStorage?) {
        userStorage?.let {
            mAdapter.setUser(userStorage)
            viewModel.normWater = userStorage.normWater.toDouble()
            viewModel.persentSred()
        }
    }

    private fun allReportChange(list: List<Report>?) {
        var sred = 0
        list?.let {
            mAdapter.setList(list.asReversed())
            viewModel.countDay = list.size.toDouble()
            Log.d("count1", "countDay"+viewModel.countDay.toString())
            for (report in list){
                if(report.allWater == "true"){
                    viewModel.count++
                }
                Log.d("count1", "sred "+ report.water.toInt())
                sred += report.water.toInt()
                Log.d("count1", "sred "+ (sred / list.size).toInt()+ " "+viewModel.countDay)
            }
            viewModel.sredWater.value = (sred / viewModel.countDay).toInt()
            viewModel.sred = (sred / viewModel.countDay).toDouble()
            viewModel.persentDay()
            viewModel.persentSred()
        }
    }
}