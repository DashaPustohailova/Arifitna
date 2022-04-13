package com.example.arifitna.ui.statistics

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arifitna.R
import com.example.arifitna.model.Report
import com.example.arifitna.model.UserStorage

class StatisticsAdapter: RecyclerView.Adapter<StatisticsHolder>() {

    private var mListReport = emptyList<Report>()
    var userData = UserStorage()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.report_item, parent, false)
        return StatisticsHolder(view)
    }

    override fun getItemCount(): Int = mListReport.size

    override fun onBindViewHolder(holder: StatisticsHolder, position: Int) {
        var normWater = userData.normWater.toDouble()
        var partWater = mListReport[position].water.toDouble()
        var procent = 0
        holder.dateReport.text = mListReport[position].date
        holder.countWaterReport.text = "Выпито ${partWater.toInt()} мл из ${normWater.toInt()} мл"
        holder.prosentReport.text =
            if(partWater > 0){
                    if(partWater < normWater){
                        procent = Math.round(partWater/(userData.normWater.toDouble()/100)).toInt()
                    }
                    else{
                        procent = 100
                    }
                procent.toString() + "%"
            }
            else
                "0%"
    }

    fun setList(list: List<Report>) {
        mListReport = list
        notifyDataSetChanged()
    }

    fun setUser(user: UserStorage) {
        userData = user
    }

}