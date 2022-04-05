package com.krivochkov.homework_2.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.krivochkov.homework_2.R

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .error(R.mipmap.ic_launcher_round)
        .into(this)
}