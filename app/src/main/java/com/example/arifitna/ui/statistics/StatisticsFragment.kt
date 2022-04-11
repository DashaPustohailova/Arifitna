package com.example.arifitna.ui.statistics

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.arifitna.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_statistics.*

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupBottomNavigation()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    recyclerViewStatistic.visibility = View.VISIBLE
                    constraintGraphicStatistic.visibility = View.GONE
                    true
                }
                R.id.page_2 -> {
                    recyclerViewStatistic.visibility = View.GONE
                    constraintGraphicStatistic.visibility = View.VISIBLE
                    true
                }
                else -> {
                    true
                }
            }
        }
    }
}