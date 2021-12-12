package com.example.weathertestapp.ui.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weathertestapp.R
import com.example.weathertestapp.data.model.Weather
import com.example.weathertestapp.databinding.ItemWeatherBinding
import com.example.weathertestapp.utils.formatDate
import kotlin.math.roundToInt

class WeatherAdapter : ListAdapter<Weather, RecyclerView.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as WeatherViewHolder).apply {
            bindItem(item)
        }
    }

    inner class WeatherViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: Weather) {
            binding.tvDate.text = item.utcTime?.formatDate()

            Glide.with(itemView.context)
                .load(item.iconLink)
                .skipMemoryCache(true)
                .into(binding.ivWeatherIcon)

            binding.tvWeather.text = item.description

            val highTemp = item.highTemp?.toDoubleOrNull()?.roundToInt()
            val lowTemp = item.lowTemp?.toDoubleOrNull()?.roundToInt()
            var highTempStr = ""
            var lowTempStr = ""
            if (lowTemp != null) {
                lowTempStr = StringBuilder()
                    .append(if (lowTemp > 0) "+" else "-")
                    .append(lowTemp.toString())
                    .toString()
            }
            if (highTemp != null) {
                highTempStr = StringBuilder()
                    .append(if (highTemp > 0) "+" else "-")
                    .append(highTemp.toString())
                    .toString()
            }
            binding.tvTemp.text = itemView.context.getString(R.string.temps_celsius, highTempStr, lowTempStr)
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<Weather>() {
        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem.utcTime == newItem.utcTime
        }

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem == newItem
        }
    }
}