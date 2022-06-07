package com.teasers.bookingapp.presentation.map


import android.location.Address
import android.location.Geocoder
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teasers.bookingapp.R
import com.teasers.bookingapp.data.model.Poi
import com.teasers.bookingapp.databinding.VehicleItemListBinding

internal class VehicleViewHolder(
    private val vehicleBinding: VehicleItemListBinding,
    private val onItemClick: (poi: Poi) -> Unit,
    private val geocoder: Geocoder,
    private val adapter: VehicleListAdapter
) : RecyclerView.ViewHolder(vehicleBinding.root) {

    var selectedItemPos = -1
    var lastItemSelectedPos = -1


    fun bind(poi: Poi?) {
        if (poi != null) {
            val addresses: List<Address> = geocoder.getFromLocation(
                poi.coordinate.latitude,
                poi.coordinate.longitude,
                1
            )
            val postal = addresses[0].postalCode
            val countryName = addresses[0].countryName
            val area = "$postal,$countryName"

            if (poi.fleetType == "TAXI") {
                vehicleBinding.vehicleImage.setImageResource(R.drawable.taxi)
            } else {
                vehicleBinding.vehicleImage.setImageResource(R.drawable.pool)
            }
            vehicleBinding.vehicleLocation1.text = addresses[0].locality
            vehicleBinding.vehicleLocation2.text = area
            vehicleBinding.vehicleCard.setOnClickListener {
                selectedItemPos = bindingAdapterPosition
                if (lastItemSelectedPos == -1)
                    lastItemSelectedPos = selectedItemPos
                else {
                    adapter.notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                }
                adapter.notifyItemChanged(selectedItemPos)
                onItemClick(poi)
                //vehicleBinding.vehicleCard.setBackgroundResource(R.drawable.background_selected)

            }

        }

    }

    fun defaultBg() {
        vehicleBinding.vehicleCard.setBackgroundResource((R.drawable.background_unselected))
    }

    fun selectedBg() {
        vehicleBinding.vehicleCard.setBackgroundResource((R.drawable.background_selected))
    }

}