package com.example.arifitna.ui.startFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arifitna.R
import com.example.arifitna.model.Drinks
import com.example.arifitna.model.UserStorage

class DrinksAdapter(private val callback: (Int) -> Unit) : RecyclerView.Adapter<DrinksHolder>()  {

    private var mListDrinks = emptyList<Drinks>()
    var userData = UserStorage()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinksHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.report_item, parent, false)
        return DrinksHolder(view)
    }

    override fun getItemCount(): Int = mListDrinks.size

    override fun onBindViewHolder(holder: DrinksHolder, position: Int) {
        holder.dateReport.text = mListDrinks[position].title
        holder.countWaterReport.text = "${mListDrinks[position].hydro}% воды"
        holder.prosentReport.text = "${mListDrinks[position].volume} мл"
        holder.itemView.setOnClickListener { callback(Math.floor(mListDrinks[position].volume.toDouble()/100*mListDrinks[position].hydro.toDouble()).toInt()) }

    }

    fun setList(list: List<Drinks>) {
        mListDrinks = list
        notifyDataSetChanged()
    }


}