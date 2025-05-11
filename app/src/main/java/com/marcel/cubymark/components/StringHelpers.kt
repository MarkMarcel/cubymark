package com.marcel.cubymark.components

import android.content.Context
import com.marcel.cubymark.R
import com.marcel.cubymark.unitconversion.models.DistanceUnit
import com.marcel.cubymark.unitconversion.models.MassUnit
import com.marcel.cubymark.unitconversion.models.TemperatureUnit

/**
 * Returns the display name for a given DistanceUnit.
 */
internal fun distanceUnitName(
    context: Context,
    unit: DistanceUnit
): String {
    return when (unit) {
        DistanceUnit.FEET -> context.getString(R.string.feet_distance_unit)
        DistanceUnit.INCHES -> context.getString(R.string.inches_distance_unit)
        DistanceUnit.METERS -> context.getString(R.string.meters_distance_unit)
    }
}

/**
 * Returns the symbol string for a given DistanceUnit.
 */
internal fun distanceUnitSymbol(
    context: Context,
    unit: DistanceUnit
): String {
    return when (unit) {
        DistanceUnit.FEET -> context.getString(R.string.feet_distance_unit_symbol)
        DistanceUnit.INCHES -> context.getString(R.string.inches_distance_unit_symbol)
        DistanceUnit.METERS -> context.getString(R.string.meters_distance_unit_symbol)
    }
}

/**
 * Returns the display name for a given MassUnit.
 */
internal fun massUnitName(
    context: Context,
    unit: MassUnit
): String {
    return when (unit) {
        MassUnit.KILOGRAMS -> context.getString(R.string.kilograms_mass_unit)
        MassUnit.POUNDS -> context.getString(R.string.pounds_mass_unit)
        MassUnit.OUNCES -> context.getString(R.string.ounces_mass_unit)
    }
}

/**
 * Returns the symbol string for a given MassUnit.
 */
internal fun massUnitSymbol(
    context: Context,
    unit: MassUnit
): String {
    return when (unit) {
        MassUnit.KILOGRAMS -> context.getString(R.string.kilograms_mass_unit_symbol)
        MassUnit.POUNDS -> context.getString(R.string.pounds_mass_unit_symbol)
        MassUnit.OUNCES -> context.getString(R.string.ounces_mass_unit_symbol)
    }
}

/**
 * Returns the display name for a given TemperatureUnit.
 */
internal fun temperatureUnitName(
    context: Context,
    unit: TemperatureUnit
): String {
    return when (unit) {
        TemperatureUnit.CELSIUS -> context.getString(R.string.celsius_temperature_unit)
        TemperatureUnit.FAHRENHEIT -> context.getString(R.string.fahrenheit_temperature_unit)
        TemperatureUnit.KELVIN -> context.getString(R.string.kelvin_temperature_unit)
    }
}

/**
 * Returns the symbol string for a given TemperatureUnit.
 */
internal fun temperatureUnitSymbol(
    context: Context,
    unit: TemperatureUnit
): String {
    return when (unit) {
        TemperatureUnit.CELSIUS -> context.getString(R.string.celsius_temperature_unit_symbol)
        TemperatureUnit.FAHRENHEIT -> context.getString(R.string.fahrenheit_temperature_unit_symbol)
        TemperatureUnit.KELVIN -> context.getString(R.string.kelvin_temperature_unit_symbol)
    }
}
