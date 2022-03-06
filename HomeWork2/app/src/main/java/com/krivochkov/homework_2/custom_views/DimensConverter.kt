package com.krivochkov.homework_2.custom_views

import android.content.Context
import android.util.TypedValue

fun Int.dpToPx(context: Context): Float {
    return toFloat().dpToPx(context)
}

fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )
}

fun Float.spToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    )
}