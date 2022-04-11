package com.example.arifitna.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.arifitna.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var mNavController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigationGraph()
        initAppBarConfiguration()
    }

    private fun initNavigationGraph() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        mNavController = navHostFragment.navController
    }

    private fun initAppBarConfiguration(){
        appBarConfiguration = AppBarConfiguration(setOf(R.id.startFragment, R.id.settingsFragment, R.id.statisticsFragment, R.id.profileFragment), drawer_layout)
        navigationView.setupWithNavController(mNavController)
        setupActionBarWithNavController(mNavController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}