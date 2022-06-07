package com.teasers.bookingapp.presentation.map

import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teasers.bookingapp.data.model.Poi
import com.teasers.bookingapp.databinding.VehicleItemListBinding

internal class VehicleListAdapter(
    private val onItemClick: (poi: Poi) -> Unit,
    private val poiList: List<Poi>,
    private val geocoder: Geocoder
) : ListAdapter<Poi, RecyclerView.ViewHolder>(PoiDiffCallback()) {

    private lateinit var binding: VehicleItemListBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        binding = VehicleItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding, onItemClick, geocoder, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == (holder as VehicleViewHolder).selectedItemPos)
            (holder as VehicleViewHolder).selectedBg()
        else
            (holder as VehicleViewHolder).defaultBg()
        (holder as VehicleViewHolder).bind(poiList[position])

    }

    override fun getItemCount(): Int {
        return poiList.size
    }

    private class PoiDiffCallback : DiffUtil.ItemCallback<Poi>() {

        override fun areItemsTheSame(oldItem: Poi, newItem: Poi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Poi, newItem: Poi): Boolean {
            return oldItem == newItem
        }
    }
}