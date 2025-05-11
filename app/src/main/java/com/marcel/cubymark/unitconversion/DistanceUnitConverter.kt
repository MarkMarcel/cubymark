package com.marcel.cubymark.unitconversion

import com.marcel.cubymark.unitconversion.models.ConversionException
import com.marcel.cubymark.unitconversion.models.DistanceUnit
import com.marcel.cubymark.unitconversion.models.Measurement
import com.marcel.cubymark.unitconversion.models.QuantityUnit

/**
 * Converts values from one [DistanceUnit] to another.
 */
class DistanceConverter : UnitConverter {
    override suspend fun convert(
        measurement: Measurement,
        targetUnit: QuantityUnit
    ): Result<Measurement> {
        return try {
            // Validate that the input unit is a DistanceUnit
            if (measurement.unit !is DistanceUnit) {
                return Result.failure(
                    ConversionException(
                        "Invalid input unit: ${measurement.unit::class.simpleName}. Expected a DistanceUnit."
                    )
                )
            }

            // Validate that the target unit is also a DistanceUnit
            if (targetUnit !is DistanceUnit) {
                return Result.failure(
                    ConversionException(
                        "Invalid target unit: ${targetUnit::class.simpleName}. Expected a DistanceUnit."
                    )
                )
            }

            // Disallow negative distance values
            if (measurement.value < 0) {
                return Result.failure(ConversionException("Negative distance values are not supported."))
            }

            // Convert the input value to a common base unit (meters)
            val valueInMeters = when (measurement.unit) {
                DistanceUnit.METERS -> measurement.value
                DistanceUnit.FEET -> measurement.value * 0.3048
                DistanceUnit.INCHES -> measurement.value * 0.0254
            }

            // Convert the value from the base unit (meters) to the target unit
            val convertedValue = when (targetUnit) {
                DistanceUnit.METERS -> valueInMeters
                DistanceUnit.FEET -> valueInMeters / 0.3048 // Meters to feet
                DistanceUnit.INCHES -> valueInMeters / 0.0254 // Meters to inches
            }

            Result.success(Measurement(convertedValue, targetUnit))

        } catch (e: Exception) {
            // Catch any unexpected errors during calculation
            Result.failure(ConversionException("Error during distance conversion: ${e.localizedMessage}"))
        }
    }
}