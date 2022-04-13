package com.example.arifitna.ui.statistics

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.arifitna.R
import com.example.arifitna.extensions.observe
import com.example.arifitna.model.Report
import com.example.arifitna.model.UserStorage
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    private fun setupRv(){
        mAdapter = StatisticsAdapter()
        mRecyclerView = recycler_view
        mRecyclerView.adapter = mAdapter
    }

    private fun setupObservers() {
        observe(viewModel.allReport, ::allReportChange)
        observe(viewModel.userData, ::userDataChange)
    }

    private fun userDataChange(userStorage: UserStorage?) {
        userStorage?.let {
            mAdapter.setUser(userStorage)
        }
    }

    private fun allReportChange(list: List<Report>?) {
        list?.let{
            mAdapter.setList(list.asReversed())
        }
    }
}