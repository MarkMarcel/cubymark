package com.marcel.cubymark.unitconversion

import com.marcel.cubymark.unitconversion.models.ConversionException
import com.marcel.cubymark.unitconversion.models.MassUnit
import com.marcel.cubymark.unitconversion.models.Measurement
import com.marcel.cubymark.unitconversion.models.QuantityUnit
import com.marcel.cubymark.unitconversion.models.TemperatureUnit


/**
 * Converts values from one [MassUnit] to another.
 */
class MassConverter : UnitConverter {
    override suspend fun convert(
        measurement: Measurement,
        targetUnit: QuantityUnit
    ): Result<Measurement> {
        return try {
            // Validate that the input unit is a MassUnit
            if (measurement.unit !is MassUnit) {
                return Result.failure(ConversionException("Invalid input unit: ${measurement.unit::class.simpleName}. Expected a MassUnit."))
            }

            // Validate that the target unit is also a MassUnit
            if (targetUnit !is MassUnit) {
                return Result.failure(ConversionException("Invalid target unit: ${targetUnit::class.simpleName}. Expected a MassUnit."))
            }

            // Disallow negative mass values
            if (measurement.value < 0) {
                return Result.failure(ConversionException("Negative mass values are not supported."))
            }

            // Convert the input value to a common base unit (kilograms)
            val valueInKilograms = when (measurement.unit) {
                MassUnit.KILOGRAMS -> measurement.value
                MassUnit.POUNDS -> measurement.value * 0.453592
                MassUnit.OUNCES -> measurement.value * 0.0283495
            }

            // Convert the value from the base unit (kilograms) to the target unit
            val convertedValue = when (targetUnit) {
                MassUnit.KILOGRAMS -> valueInKilograms
                MassUnit.POUNDS -> valueInKilograms / 0.453592
                MassUnit.OUNCES -> valueInKilograms / 0.0283495
            }

            Result.success(Measurement(convertedValue, targetUnit))

        } catch (e: Exception) {
            // Catch any unexpected errors during calculation
            Result.failure(ConversionException("Error during mass conversion: ${e.localizedMessage}"))
        }
    }
}