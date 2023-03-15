package com.arch.template.utils

import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt

fun Context.IntToDp(value: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics
    ).roundToInt()
}