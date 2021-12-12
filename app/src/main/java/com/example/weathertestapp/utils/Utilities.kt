package com.example.weathertestapp.utils

import android.content.Context
import android.graphics.Color.red
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.weathertestapp.R
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utilities {

    /**
     * This function helps to display error in a snackBar
     */

    fun displayErrorSnackBar(view: View, msg: String, context: Context) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            .withColor(ContextCompat.getColor(context, R.color.red))
            .setTextColor(ContextCompat.getColor(context, R.color.white))
            .show()
    }

    /**
     * This function helps to set the color of the snackBar as used in 'displayErrorSnackBar' function above
     */

    private fun Snackbar.withColor(@ColorInt colorInt: Int): Snackbar {
        this.view.setBackgroundColor(colorInt)
        return this
    }
}