package com.marcel.cubymark.unitconversion.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marcel.cubymark.CUByMarkApplication
import com.marcel.cubymark.unitconversion.data.UnitConversionRepository
import com.marcel.cubymark.unitconversion.models.ConversionException
import com.marcel.cubymark.unitconversion.models.DistanceUnit
import com.marcel.cubymark.unitconversion.models.MassUnit
import com.marcel.cubymark.unitconversion.models.Measurement
import com.marcel.cubymark.unitconversion.models.QuantityType
import com.marcel.cubymark.unitconversion.models.QuantityUnit
import com.marcel.cubymark.unitconversion.models.TemperatureUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Represents the current state of the unit conversion screen.
 */
data class UnitConversionScreenState(
    val conversionQuantityType: QuantityType = QuantityType.DISTANCE,
    val convertFromUnit: QuantityUnit = DistanceUnit.METERS,
    val convertFromValue: String = "",
    val convertToUnit: QuantityUnit = DistanceUnit.FEET,
    val convertToValue: String = "",
    val isCalculating: Boolean = false,
    val message: String? = null
)

class UnitConversionScreenViewModel(
    private val unitConversionRepository: UnitConversionRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CUByMarkApplication)
                val unitConversionRepository = application.appContainer.unitConversionRepository
                UnitConversionScreenViewModel(unitConversionRepository)
            }
        }
    }

    val state: StateFlow<UnitConversionScreenState>
        get() = _state.asStateFlow()

    private val _state = MutableStateFlow(
        UnitConversionScreenState()
    )

    fun onQuantityTypeSelected(quantityType: QuantityType) {
        // Get the initial units for the selected quantity type
        val initialUnits = getInitialUnitsForQuantityType(quantityType)
        _state.update { currentState ->
            currentState.copy(
                conversionQuantityType = quantityType,
                convertFromUnit = initialUnits!!.first,
                convertToUnit = initialUnits.second,
                convertFromValue = "",
                convertToValue = "",
                message = null
            )
        }
    }

    fun onConvertFromUnitChange(unit: QuantityUnit) {
        _state.update { currentState ->
            if (isUnitMatchingQuantityType(unit, currentState.conversionQuantityType)) {
                currentState.copy(
                    convertFromUnit = unit,
                    convertToValue = "",
                    message = null
                )
            } else {
                currentState.copy(
                    message = "Selected unit does not match the quantity type."
                )
            }
        }
    }

    fun onConvertToUnitChange(unit: QuantityUnit) {
        _state.update { currentState ->
            if (isUnitMatchingQuantityType(unit, currentState.conversionQuantityType)) {
                currentState.copy(
                    convertToUnit = unit,
                    convertToValue = "",
                    message = null
                )
            } else {
                // Handle invalid unit selection
                currentState.copy(
                    message = "Selected unit does not match the quantity type."
                )
            }
        }
    }

    fun onConvertFromValueChange(value: String) {
        _state.update { currentState ->
            currentState.copy(
                convertFromValue = value,
                // Clear the converted value whenever the input value changes
                convertToValue = "",
                message = null
            )
        }
    }

    fun onSwapUnits() {
        _state.update { currentState ->
            currentState.copy(
                convertFromUnit = currentState.convertToUnit,
                convertToUnit = currentState.convertFromUnit,
                convertFromValue = currentState.convertToValue,
                convertToValue = currentState.convertFromValue
            )
        }
    }

    fun onConvertClicked() {
        viewModelScope.launch {
            val currentState = _state.value
            // Basic validation before attempting conversion
            if (currentState.convertFromUnit == null || currentState.convertToUnit == null) {
                _state.update { it.copy(message = "Please select both units.") }
                return@launch
            }

            val valueToConvert = currentState.convertFromValue.toDoubleOrNull()
            if (valueToConvert == null) {
                _state.update { it.copy(message = "Invalid value entered.") }
                return@launch
            }

            val measurementToConvert = Measurement(valueToConvert, currentState.convertFromUnit)

            // Perform the conversion based on the current quantity type
            val conversionResult = when (currentState.conversionQuantityType) {
                QuantityType.DISTANCE -> {
                    if (currentState.convertToUnit is DistanceUnit) {
                        unitConversionRepository.convertDistance(
                            measurementToConvert,
                            currentState.convertToUnit
                        )
                    } else {
                        Result.failure(ConversionException("Target unit does not match Distance quantity type."))
                    }
                }

                QuantityType.MASS -> {
                    if (currentState.convertToUnit is MassUnit) {
                        unitConversionRepository.convertMass(
                            measurementToConvert,
                            currentState.convertToUnit
                        )
                    } else {
                        Result.failure(ConversionException("Target unit does not match Mass quantity type."))
                    }
                }

                QuantityType.TEMPERATURE -> {
                    if (currentState.convertToUnit is TemperatureUnit) {
                        unitConversionRepository.convertTemperature(
                            measurementToConvert,
                            currentState.convertToUnit
                        )
                    } else {
                        Result.failure(ConversionException("Target unit does not match Temperature quantity type."))
                    }
                }
            }

            // Update the state based on the conversion result
            conversionResult.fold(
                onSuccess = { convertedMeasurement ->
                    // Format the converted value to two decimal places
                    val formattedValue =
                        String.format(Locale.getDefault(), "%.2f", convertedMeasurement.value)
                    _state.update {
                        it.copy(
                            convertToValue = formattedValue,
                            message = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(
                            convertToValue = "",
                            message = throwable.message ?: "Unknown conversion error"
                        )
                    }
                }
            )
        }
    }

    // Helper function to validate if a unit matches a quantity type
    private fun isUnitMatchingQuantityType(
        unit: QuantityUnit,
        quantityType: QuantityType
    ): Boolean {
        return when (quantityType) {
            QuantityType.DISTANCE -> unit is DistanceUnit
            QuantityType.MASS -> unit is MassUnit
            QuantityType.TEMPERATURE -> unit is TemperatureUnit
        }
    }
}

/**
 * Returns the first two corresponding QuantityUnit enums for a given QuantityType.
 * Useful for providing default unit selections.
 *
 * @param quantityType The QuantityType for which to get the initial units.
 * @return A Pair containing the first and second QuantityUnit enums, or null if the quantity type is unknown or has less than two units.
 */
private fun getInitialUnitsForQuantityType(quantityType: QuantityType): Pair<QuantityUnit, QuantityUnit>? {
    return when (quantityType) {
        QuantityType.DISTANCE -> {
            if (DistanceUnit.entries.size >= 2) {
                Pair(DistanceUnit.entries[0], DistanceUnit.entries[1])
            } else {
                null
            }
        }

        QuantityType.MASS -> {
            if (MassUnit.entries.size >= 2) {
                Pair(MassUnit.entries[0], MassUnit.entries[1])
            } else {
                null
            }
        }

        QuantityType.TEMPERATURE -> {
            if (TemperatureUnit.entries.size >= 2) {
                Pair(TemperatureUnit.entries[0], TemperatureUnit.entries[1])
            } else {
                null
            }
        }
    }
}






