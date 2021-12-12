package com.example.weathertestapp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.example.weathertestapp.databinding.TextHeaderViewBinding

class TextHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding =
        TextHeaderViewBinding
            .inflate(LayoutInflater.from(context), this, true)

    var city: CharSequence?
        get() = binding.tvCity.text
        set(value) {
            binding.tvCity.text = value
        }

    var date: CharSequence?
        get() = binding.tvDate.text
        set(value) {
            binding.tvDate.text = value
        }

    var onBackClickListener: OnClickListener? = null
        set(value) {
            field = value
            binding.ivBackIcon.setOnClickListener(value)
            binding.tvBack.setOnClickListener(value)
            binding.ivBackIcon.isVisible = value != null
            binding.tvBack.isVisible = value != null
        }
}