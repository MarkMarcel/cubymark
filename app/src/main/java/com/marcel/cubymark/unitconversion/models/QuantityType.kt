package com.marcel.cubymark.unitconversion.models

enum class QuantityType {
    DISTANCE,
    MASS,
    TEMPERATURE
}

sealed interface QuantityUnit

enum class DistanceUnit : QuantityUnit {
    FEET,
    INCHES,
    METERS
}

enum class MassUnit : QuantityUnit {
    KILOGRAMS,
    OUNCES,
    POUNDS
}

enum class TemperatureUnit : QuantityUnit {
    CELSIUS,
    FAHRENHEIT,
    KELVIN
}