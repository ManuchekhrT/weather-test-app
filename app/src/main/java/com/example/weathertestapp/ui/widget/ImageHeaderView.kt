package com.example.weathertestapp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.weathertestapp.databinding.ImageHeaderViewBinding

class ImageHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val binding =
        ImageHeaderViewBinding
            .inflate(LayoutInflater.from(context), this, true)

    var temp: CharSequence?
        get() = binding.tvTemp.text
        set(value) {
            binding.tvTemp.text = value
        }

    var weatherDescription: CharSequence?
        get() = binding.tvWeatherDescription.text
        set(value) {
            binding.tvWeatherDescription.text = value
        }
}