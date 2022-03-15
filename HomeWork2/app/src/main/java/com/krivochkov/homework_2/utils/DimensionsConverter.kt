package com.krivochkov.homework_2.utils

import android.content.Context
import android.util.TypedValue

fun Int.dpToPx(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
)