package com.teasers.bookingapp.presentation.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.teasers.bookingapp.R
import com.teasers.bookingapp.Utils.showDialog
import com.teasers.bookingapp.databinding.FragmentMapsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.teasers.bookingapp.data.Result
import com.teasers.bookingapp.data.model.Poi
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.teasers.bookingapp.Logging.Tracer
import java.util.*

/**
 * Displays Map &
 * recycler view with
 * all vehicles.
 *
 */
@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val TAG = MapsFragment::class.java.simpleName

    private lateinit var mFragmentMapsBinding: FragmentMapsBinding

    private lateinit var mMapView: MapView

    private lateinit var mGoogleMap: GoogleMap

    private val markerPadding = 100

    private lateinit var mVehicleRecyclerView: RecyclerView

    private val mMapViewModel: MapViewModel by viewModels()

    private val mMarkerList = mutableListOf<Marker>()

    //private val mVehicleMap = mutableMapOf<String, MutableList<LatLng>>()

    private var mMarker: Marker? = null

    private val p1Lat = 53.694865
    private val p1Lon = 9.757589
    private val p2Lat = 53.394655
    private val p2Lon = 10.099891

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentMapsBinding = FragmentMapsBinding.inflate(inflater, container, false)
        return mFragmentMapsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = mFragmentMapsBinding.map
        mVehicleRecyclerView = mFragmentMapsBinding.vehicles
        viewLifecycleOwner.lifecycleScope.launch {
            createMap()
        }
        loadVehicleData()
        showList()
    }

    private suspend fun createMap() {
        mMapView.onCreate(null)
        withContext(Dispatchers.Default) {
            mMapView.onResume()
            try {
                MapsInitializer.initialize(requireContext())
            } catch (e: GooglePlayServicesNotAvailableException) {
                Tracer.e(TAG, e.localizedMessage)
                showErrorDialog()
            }

            withContext(Dispatchers.Main) {
                mMapView.getMapAsync(callback)
            }
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * camera animated to hamburg location
         */
        mGoogleMap = googleMap
        val latLngBuilder = LatLngBounds.builder()
        val latLngBounds = LatLngBounds(LatLng(p2Lat, p2Lon), LatLng(p1Lat, p1Lon))
        latLngBuilder.include(latLngBounds.northeast)
        latLngBuilder.include(latLngBounds.southwest)

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                latLngBuilder.build(),
                markerPadding
            )
        )
    }

    private fun loadVehicleData() {
        mMapViewModel.loadAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)
    }

    /**
     * Show Vehicles in recycler view
     * and on Map else error dialog
     * if couldnt fetch data
     */
    private fun showList() {
        mMapViewModel.vehicleList.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    setUpRecyclerView(it.data)
                    showAllVehicles(it.data)
                }
                is Result.Error -> {
                    showErrorDialog()
                }
            }
        })
    }

    private fun showErrorDialog() {
        showDialog(
            requireContext(), "", getString(R.string.something_went_wrong),
            getString(R.string.ok), ""
        )
    }

    private fun setUpRecyclerView(list: List<Poi>) {
        val mVehicleAdapter = VehicleListAdapter(
            { poi -> onVehicleSelected(poi) }, list,
            Geocoder(requireContext(), Locale.getDefault())
        )
        mVehicleRecyclerView.apply {
            adapter = mVehicleAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

    }

    private fun showAllVehicles(list: List<Poi>) {
        list.forEach {
            if (it.fleetType == VehicleType.TAXI.type) {
                val latLng = LatLng(it.coordinate.latitude, it.coordinate.longitude)
                mMarker = mGoogleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(getTaxiBitmap(requireContext())))
                )
                mMarker?.let { it -> mMarkerList.add(it) }
            } else {
                val latLng = LatLng(it.coordinate.latitude, it.coordinate.longitude)
                mMarker = mGoogleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(getPoolBitmap(requireContext())))
                )
                mMarker?.let { it -> mMarkerList.add(it) }
            }
        }
    }

    private fun getPoolBitmap(context: Context): Bitmap {
        val bitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.pool_marker)
        return Bitmap.createScaledBitmap(bitmap, 100, 100, false)
    }

    private fun getTaxiBitmap(context: Context): Bitmap {
        val bitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.taxi_marker)
        return Bitmap.createScaledBitmap(bitmap, 100, 100, false)
    }

    private fun removeAllMarkers() {
        viewLifecycleOwner.lifecycleScope.launch {
            mMarkerList.forEach {
                it.remove()
            }
            mMarkerList.clear()
        }
    }


    private fun onVehicleSelected(poi: Poi) {
        val location = LatLng(poi.coordinate.latitude, poi.coordinate.longitude)
        val cameraPosition = CameraPosition.Builder().target(location).zoom(15.5f).build()
        removeAllMarkers()
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        if (poi.fleetType == VehicleType.TAXI.type) {
            mMarker = mGoogleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(getTaxiBitmap(requireContext())))
            )
            mMarker?.let { it -> mMarkerList.add(it) }
        } else {
            mMarker = mGoogleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(getPoolBitmap(requireContext())))
            )
            mMarker?.let { it -> mMarkerList.add(it) }
        }
    }


}