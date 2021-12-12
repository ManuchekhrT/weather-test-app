package com.example.weathertestapp.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun String.formatDate(): String {
    val inputFormat = SimpleDateFormat(Constants.INPUT_FORMAT_DATE_PATTERN, Locale.ENGLISH)
    val outputFormat = SimpleDateFormat(Constants.OUTPUT_FORMAT_DATE_PATTERN, Locale.ENGLISH)
    try {
        return outputFormat.format(inputFormat.parse(this) ?: "")
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    return ""
}

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this.time)
}

fun String.toDate(currentFormat: String = "yyyy-MM-dd"): Date {
    return try {
        if (this.isBlank()) {
            Date()
        } else {
            SimpleDateFormat(currentFormat, Locale.ENGLISH).parse(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        //If It can not be parsed, return today's date instead of null.
        //So return value of this method does not create null pointer exception.
        Date()
    }
}