package com.teasers.bookingapp.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teasers.bookingapp.data.model.Poi
import com.teasers.bookingapp.domain.usecases.GetAllVehicleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.teasers.bookingapp.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MapViewModel @Inject constructor(private val vehicleUseCase: GetAllVehicleUseCase) :
    ViewModel() {

    private val _VehicleList = MutableLiveData<Result<List<Poi>>>()
    val vehicleList: LiveData<Result<List<Poi>>>
        get() = _VehicleList


    fun loadAllVehicles(
        p1Lat: Double,
        p1Lon: Double,
        p2Lat: Double,
        p2Lon: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
          val result  = vehicleUseCase.invoke(
                p1Lat,
                p1Lon,
                p2Lat,
                p2Lon
            )
            withContext(Dispatchers.Main){
                _VehicleList.value = result
            }
        }
    }
}