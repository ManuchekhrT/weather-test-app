package com.example.weathertestapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.weathertestapp.MainActivity
import com.example.weathertestapp.R
import com.example.weathertestapp.data.network.NetworkResponse
import com.example.weathertestapp.databinding.FragmentSelectCityBinding
import com.example.weathertestapp.ui.presentation.SelectCityViewModel
import com.example.weathertestapp.utils.Utilities
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class SelectCityFragment : Fragment() {

    private var _binding: FragmentSelectCityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SelectCityViewModel by viewModels()

    //for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSelectCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()

        binding.autoCompleteTvSelectCity.doOnTextChanged { text, start, before, count ->
            viewModel.doSearchLocalities(text.toString())
        }

        binding.autoCompleteTvSelectCity.setOnItemClickListener { adapterView, view, position, l ->
            setAutoCompleteText(adapterView.getItemAtPosition(position) as String)
            updateFocus()
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.btnCurrentLocation.setOnClickListener {
            prepareCurrentLocation()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.city.collect {
                    binding.btnOk.isVisible = it.isNotBlank()
                }
            }
        }

        binding.btnOk.setOnClickListener {
            if (viewModel.city.value.isNotBlank()) {
                findNavController().navigate(
                    SelectCityFragmentDirections.toShowWeatherFragment(city = viewModel.city.value)
                )
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchLocalities.collect { response ->
                    when (response) {
                        is NetworkResponse.Loading -> {

                        }
                        is NetworkResponse.Success -> {
                            val localities = mutableListOf<String>()
                            response.data?.items?.let { items ->
                                items.forEach {
                                    if (it.address.city != null)
                                        localities.add(it.address.city)
                                }
                            }
                            val adapter =  ArrayAdapter(
                                requireContext(),
                                R.layout.item_locality,
                                R.id.autocomplete_text,
                                localities
                            )
                            binding.autoCompleteTvSelectCity.setAdapter(adapter)
                            adapter.notifyDataSetChanged()
                        }
                        is NetworkResponse.Error -> {
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
    }

    private fun updateFocus() {
        binding.autoCompleteTvSelectCity.clearFocus()
        binding.parentContainer.requestFocus()
    }

    private fun prepareCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requireActivity().checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && requireActivity().checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requireActivity().requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                locationPermissionCode
            )
            return
        }

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                val geocoder = Geocoder(requireContext(), Locale.ENGLISH)
                try {
                    val addresses = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )
                    if (addresses.size > 0) {
                        val city = addresses[0].locality
                        if (!city.isNullOrBlank()) {
                            setAutoCompleteText(city)
                            updateFocus()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .addOnFailureListener {
                Utilities.displayErrorSnackBar(
                    requireView(),
                    resources.getString(R.string.failed_retrieving_location),
                    requireContext()
                )
            }
    }

    private fun setAutoCompleteText(city: String) {
        viewModel.setCity(city)
        binding.autoCompleteTvSelectCity.setText(city)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prepareCurrentLocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val locationPermissionCode = 1001
    }
}