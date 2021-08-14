package com.yash2108.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yash2108.weatherapp.databinding.LayoutWeatherInfoItemBinding
import com.yash2108.weatherapp.models.AdapterObject
import javax.inject.Inject

class HomeAdapter @Inject constructor() :
    ListAdapter<AdapterObject, HomeAdapter.ViewHolder>(HomeAdapterDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutWeatherInfoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bindView(getItem(position), position)
    }

    inner class ViewHolder(val binding: LayoutWeatherInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(data: AdapterObject, position: Int) {
            binding.tvTitle.text = data.title
            binding.tvValue.text = data.subtitle
            //
            if (position == 0) binding.divider.visibility = View.VISIBLE
            else binding.divider.visibility = View.GONE
        }
    }


    class HomeAdapterDiffUtil() : DiffUtil.ItemCallback<AdapterObject>() {

        override fun areItemsTheSame(oldItem: AdapterObject, newItem: AdapterObject): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: AdapterObject, newItem: AdapterObject): Boolean {
            return oldItem == newItem
        }
    }
}