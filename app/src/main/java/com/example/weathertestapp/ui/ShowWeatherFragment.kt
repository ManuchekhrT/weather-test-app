package com.example.weathertestapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathertestapp.MainActivity
import com.example.weathertestapp.R
import com.example.weathertestapp.data.network.NetworkResponse
import com.example.weathertestapp.databinding.FragmentShowWeatherBinding
import com.example.weathertestapp.ui.presentation.ShowWeatherViewModel
import com.example.weathertestapp.ui.widget.WeatherAdapter
import com.example.weathertestapp.utils.Utilities
import com.example.weathertestapp.utils.formatDate
import com.example.weathertestapp.utils.isToday
import com.example.weathertestapp.utils.toDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class ShowWeatherFragment : Fragment() {
    private var _binding: FragmentShowWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShowWeatherViewModel by viewModels()
    private val weatherAdapter = WeatherAdapter()
    private val args: ShowWeatherFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentShowWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()

        viewModel.fetchDailyForecastWeather(args.city ?: "")
        binding.rvWeatherForecast.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = weatherAdapter
        }

        binding.imageHeaderView.binding.textHeaderView.onBackClickListener = View.OnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherList.collect { response ->
                    when (response) {
                        is NetworkResponse.Loading -> {
                            showLoading(true)
                        }
                        is NetworkResponse.Success -> {
                            response.let { resp ->
                                val weathers = resp.data?.dailyForecasts?.forecast?.forecastWeathers
                                val todayWeather = if (!weathers.isNullOrEmpty()) {
                                    weathers.firstOrNull { aWeather ->
                                        aWeather.utcTime!!.toDate().isToday()
                                    }.let { aFilteredWeather ->
                                        aFilteredWeather ?: weathers.firstOrNull()
                                    }
                                } else {
                                    null
                                }
                                val temp = todayWeather?.highTemp?.toDoubleOrNull()?.roundToInt()
                                val tempStr =
                                    resources.getString(R.string.temp_selsius, temp.toString())
                                binding.imageHeaderView.temp = tempStr
                                binding.imageHeaderView.weatherDescription =
                                    todayWeather?.description
                                binding.imageHeaderView.binding.textHeaderView.city = args.city
                                binding.imageHeaderView.binding.textHeaderView.date =
                                    todayWeather?.utcTime?.formatDate()

                                val otherWeathers = weathers?.filter { aWeather ->
                                    aWeather != todayWeather
                                }
                                weatherAdapter.submitList(otherWeathers)
                                showLoading(false)
                                binding.swipeRefreshContainer.isRefreshing = false

                            }
                        }
                        is NetworkResponse.Error -> {
                            showLoading(true)
                            Utilities.displayErrorSnackBar(
                                requireView(),
                                response.message ?: "",
                                requireContext()
                            )
                        }
                    }
                }
            }
        }

        binding.swipeRefreshContainer.setOnRefreshListener {
            binding.swipeRefreshContainer.isRefreshing = true
            viewModel.fetchDailyForecastWeather(args.city ?: "")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.isVisible = isLoading
        binding.viewLoadingCover.isVisible = isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}