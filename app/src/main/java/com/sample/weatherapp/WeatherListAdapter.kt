package com.sample.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.weatherapp.databinding.ItemReviewBinding
import com.sample.weatherapp.network.model.ResponseData

class WeatherListAdapter(
    private val mContext: Context,
    private val callback: (ResponseData) -> Unit
) : RecyclerView.Adapter<WeatherListAdapter.WeatherHolder>() {

    private val list = ArrayList<ResponseData>()

    @SuppressLint("NotifyDataSetChanged")
    fun resetList(list: List<ResponseData>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class WeatherHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { callback(list[adapterPosition]) }
        }

        fun bindData(data: ResponseData) {
            binding.tvWeatherType.text = data.weather?.get(0)?.main.toString()
            binding.tvTemperature.text = (data.main?.temp ?: 0.0).toInt().toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WeatherHolder(ItemReviewBinding.inflate(LayoutInflater.from(mContext), parent, false))

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) = holder.bindData(list[position])

    override fun getItemCount() = list.size
}