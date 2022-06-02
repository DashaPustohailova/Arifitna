package com.example.arifitna.ui.startFragment

import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arifitna.use_case.UpdateCountWaterUseCase
import com.example.arifitna.use_case.UpdatePersonalDataUseCase
import kotlinx.android.synthetic.main.report_item.view.*
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class DrinksHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    val dateReport: TextView = itemView.item_date
    val countWaterReport: TextView = itemView.item_count_water
    val prosentReport: TextView = itemView.procent


}