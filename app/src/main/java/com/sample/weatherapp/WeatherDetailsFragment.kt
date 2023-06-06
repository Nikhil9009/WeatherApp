package com.sample.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sample.weatherapp.databinding.WeatherDetailsBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class WeatherDetailsFragment : Fragment() {

    private var _binding: WeatherDetailsBinding? = null
    private val detailsData by lazy { WeatherDetailsFragmentArgs.fromBundle(requireArguments()).ArgumnetDetails }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = WeatherDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTemperature.text = detailsData.temperature
        binding.tvFeelsLike.text = "Feels Like: ${detailsData.feelsLikeTemperature}"
        binding.tvMain.text = detailsData.main
        binding.tvDescription.text = detailsData.description
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).updateScreenTitle(detailsData.screenTitle.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}