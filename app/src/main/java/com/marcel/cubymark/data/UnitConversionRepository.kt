package com.marcel.cubymark.data

import com.marcel.cubymark.unitconversion.DistanceConverter
import com.marcel.cubymark.unitconversion.MassConverter
import com.marcel.cubymark.unitconversion.TemperatureConverter
import com.marcel.cubymark.unitconversion.models.DistanceUnit
import com.marcel.cubymark.unitconversion.models.Measurement

// No interface created because there's isn't really any state for the app
class UnitConversionRepository(
    private val distanceConverter: DistanceConverter,
    private val massConverter: MassConverter,
    private val temperatureConverter: TemperatureConverter
) {
    suspend fun convertDistance(
        measurement: Measurement,
        targetUnit: DistanceUnit
    ): Result<Measurement> {
        return distanceConverter.convert(measurement, targetUnit)
    }

    suspend fun convertMass(
        measurement: Measurement,
        targetUnit: DistanceUnit
    ): Result<Measurement> {
        return massConverter.convert(measurement, targetUnit)
    }

    suspend fun convertTemperature(
        measurement: Measurement,
        targetUnit: DistanceUnit
    ): Result<Measurement> {
        return temperatureConverter.convert(measurement, targetUnit)
    }
}