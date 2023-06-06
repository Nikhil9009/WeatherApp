package com.sample.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.sample.weatherapp.databinding.WeatherListBinding
import com.sample.weatherapp.network.APIClient
import com.sample.weatherapp.network.APIRequest
import com.sample.weatherapp.network.model.DetailsDataModel
import com.sample.weatherapp.network.model.ResponseData
import com.sample.weatherapp.utils.CommonUtils
import rx.android.schedulers.AndroidSchedulers

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class WeatherListFragment : Fragment() {

    private var _binding: WeatherListBinding? = null
    private val adapter by lazy { WeatherListAdapter(requireActivity(), ::onWeatherClick) }
    private val cityName by lazy { WeatherListFragmentArgs.fromBundle(requireArguments()).CityName }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = WeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun onWeatherClick(weather: ResponseData) {
        val dataModel = DetailsDataModel(
            temperature = weather.main?.temp?.toInt().toString(),
            feelsLikeTemperature = weather.main?.feelsLike?.toInt().toString(),
            main = weather.weather?.get(0)?.main,
            description = weather.weather?.get(0)?.description,
            screenTitle = cityName
        )
        findNavController().navigate(WeatherListFragmentDirections.actionWeatherListFragmentToWeatherDetailsFragment(dataModel))
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvAllWeathers.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvAllWeathers.adapter = adapter

        if (CommonUtils.isNetworkAvailable(requireActivity())) {
            showProgress(requireActivity())
            val queryMap = HashMap<String, String>()
            queryMap["q"] = cityName
            queryMap["appid"] = BuildConfig.API_KEY
            val requestClient = APIClient.getRetrofitAdapter(BuildConfig.BASE_URL).create(APIRequest::class.java)
            requestClient.getCityWeather(queryMap).observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                hideProgress()
                if (response.cod == "200" && response.list?.isNotEmpty() == true) {
                    binding.tvEmptyView.visibility = View.GONE
                    adapter.resetList(response.list)
                } else {
                    binding.tvEmptyView.text = "No Data Found"
                    binding.tvEmptyView.visibility = View.VISIBLE
                    Snackbar.make(requireActivity(), binding.root, "No Data Found", Snackbar.LENGTH_SHORT).show()
                }
            }, { error ->
                hideProgress()
                binding.tvEmptyView.text = "Unknown Error"
                binding.tvEmptyView.visibility = View.VISIBLE
                Snackbar.make(requireActivity(), binding.root, error.message ?: "Unknown Error", Snackbar.LENGTH_SHORT).show()
            })
        } else Snackbar.make(
            requireActivity(),
            binding.root,
            "No Internet connection. Make sure that Wi-Fi or cellular mobile data is turned on, then try again.",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private var dialog: AlertDialog? = null

    private fun showProgress(context: Context) {
        if (dialog == null) {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setView(R.layout.progress_bar)
            builder.setCancelable(false)
            dialog = builder.create()
            dialog?.let {
                it.requestWindowFeature(Window.FEATURE_NO_TITLE)
                it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
        dialog?.let {
            if (!it.isShowing) it.show()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).updateScreenTitle(cityName)
    }

    private fun hideProgress() {
        dialog?.let { if (it.isShowing) it.dismiss() }
        dialog = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}