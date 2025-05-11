package com.marcel.cubymark.unitconversion

import com.marcel.cubymark.unitconversion.models.ConversionException
import com.marcel.cubymark.unitconversion.models.Measurement
import com.marcel.cubymark.unitconversion.models.QuantityUnit
import com.marcel.cubymark.unitconversion.models.TemperatureUnit

/**
 * Converts units of measurement within the Temperature quantity type.
 */
class TemperatureConverter : UnitConverter {
    override suspend fun convert(
        measurement: Measurement,
        targetUnit: QuantityUnit
    ): Result<Measurement> {
        return try {
            // Validate that the input unit is a TemperatureUnit
            if (measurement.unit !is TemperatureUnit) {
                return Result.failure(ConversionException("Invalid input unit: ${measurement.unit::class.simpleName}. Expected a TemperatureUnit."))
            }

            // Validate that the target unit is also a TemperatureUnit
            if (targetUnit !is TemperatureUnit) {
                return Result.failure(ConversionException("Invalid target unit: ${targetUnit::class.simpleName}. Expected a TemperatureUnit."))
            }

            // Convert the input value to a common base unit (Kelvin)
            val valueInKelvin = when (measurement.unit) {
                TemperatureUnit.KELVIN -> measurement.value
                TemperatureUnit.CELSIUS -> measurement.value + 273.15
                TemperatureUnit.FAHRENHEIT -> (measurement.value - 32) * 5.0 / 9.0 + 273.15
            }

            // Convert the value from the base unit (Kelvin) to the target unit
            val convertedValue = when (targetUnit) {
                TemperatureUnit.KELVIN -> valueInKelvin
                TemperatureUnit.CELSIUS -> valueInKelvin - 273.15
                TemperatureUnit.FAHRENHEIT -> (valueInKelvin - 273.15) * 9.0 / 5.0 + 32
            }

            Result.success(Measurement(convertedValue, targetUnit))

        } catch (e: Exception) {
            // Catch any unexpected errors during calculation
            Result.failure(ConversionException("Error during temperature conversion: ${e.localizedMessage}"))
        }
    }
}