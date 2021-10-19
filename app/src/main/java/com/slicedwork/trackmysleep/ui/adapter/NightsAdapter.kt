package com.slicedwork.trackmysleep.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slicedwork.trackmysleep.R
import com.slicedwork.trackmysleep.data.model.Night
import com.slicedwork.trackmysleep.databinding.ItemNightBinding

class NightsAdapter(private val nights: List<Night>) : RecyclerView.Adapter<NightsAdapter.NightViewHolder>() {

    private lateinit var itemNightBinding: ItemNightBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NightViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        itemNightBinding = ItemNightBinding.inflate(inflater, parent, false)

        return NightViewHolder(itemNightBinding)
    }

    override fun onBindViewHolder(nightViewHolder: NightViewHolder, position: Int) {
        nightViewHolder.bind(nights[position])
    }

    override fun getItemCount(): Int {
        return nights.size
    }

    inner class NightViewHolder(itemNightBinding: ItemNightBinding) :
        RecyclerView.ViewHolder(itemNightBinding.root) {
        private val startTime = itemNightBinding.tvStartTime
        private val stopTime = itemNightBinding.tvStopTime
        private val sleepQuality = itemNightBinding.tvSleepQuality
        private val sleepQualityIcon = itemNightBinding.ivSleepQuality
        private val duration = itemNightBinding.tvDuration
        fun bind(night: Night) {
            startTime.text = night.startTime
            stopTime.text = night.endTime
            sleepQuality.text = night.sleepQuality
            duration.text = night.duration

            Glide.with(itemView.context)
                .load(night.sleepQualityIcon)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .into(sleepQualityIcon)
        }
    }
}