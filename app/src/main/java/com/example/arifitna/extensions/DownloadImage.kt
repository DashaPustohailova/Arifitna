package com.example.arifitna.extensions

import com.example.arifitna.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*

fun CircleImageView.downloadImage(photoUrl: String){
    Picasso.get()
        .load(photoUrl)
        .placeholder(R.drawable.ic_baseline_person_24)
        .into(this)
}