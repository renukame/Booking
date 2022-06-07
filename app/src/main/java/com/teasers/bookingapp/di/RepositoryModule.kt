package com.teasers.bookingapp.di

import com.teasers.bookingapp.data.repository.VehicleRepositoryImpl
import com.teasers.bookingapp.domain.repository.VehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Module class that provides instance of Repository.
 *
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun vehicleRepository(vehicleRepository: VehicleRepositoryImpl): VehicleRepository
}