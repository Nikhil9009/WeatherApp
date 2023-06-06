package com.sample.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sample.weatherapp.databinding.SearchCityBinding
import com.sample.weatherapp.utils.CommonUtils

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CitySearchFragment : Fragment() {

    private var _binding: SearchCityBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SearchCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btLookup.setOnClickListener {
            CommonUtils.hideKeyboard(requireActivity())
            if (binding.etCityName.text?.isEmpty() == true) {
                Snackbar.make(requireActivity(), binding.root, "City name should not be empty", Snackbar.LENGTH_SHORT).show()
            } else if ((binding.etCityName.text?.length ?: 0) < 2) {
                Snackbar.make(requireActivity(), binding.root, "City name should atleast 2 characters length", Snackbar.LENGTH_SHORT).show()
            } else {
                val action = CitySearchFragmentDirections.actionCitySearchFragmentToWeatherListFragment(binding.etCityName.text?.trimEnd().toString())
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}