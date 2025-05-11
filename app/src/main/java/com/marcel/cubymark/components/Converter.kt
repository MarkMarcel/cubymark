package com.marcel.cubymark.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.marcel.cubymark.R
import com.marcel.cubymark.theme.baseSpacing
import com.marcel.cubymark.unitconversion.models.DistanceUnit
import com.marcel.cubymark.unitconversion.models.MassUnit
import com.marcel.cubymark.unitconversion.models.QuantityType
import com.marcel.cubymark.unitconversion.models.QuantityUnit
import com.marcel.cubymark.unitconversion.models.TemperatureUnit

@Composable
fun TwoWayConverter(
    modifier: Modifier = Modifier,
    convertFromQuantityType: QuantityType,
    convertFromUnit: QuantityUnit?,
    convertFromValue: String,
    convertToQuantityType: QuantityType,
    convertToUnit: QuantityUnit?,
    convertToValue: String,
    onConvertFromValueChange: (value: String) -> Unit,
    onConvertFromUnitChange: (unit: QuantityUnit) -> Unit,
    onConvertToUnitChange: (unit: QuantityUnit) -> Unit,
    onSwapUnits: () -> Unit,
    onConvertClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(vertical = baseSpacing)
    ) {
        UnitsSelection(
            modifier = Modifier.fillMaxWidth(),
            convertFromQuantityType = convertFromQuantityType,
            convertFromUnit = convertFromUnit,
            convertToQuantityType = convertToQuantityType,
            convertToUnit = convertToUnit,
            onConvertFromUnitChange = onConvertFromUnitChange,
            onConvertToUnitChange = onConvertToUnitChange,
            onSwapUnits = onSwapUnits
        )
        Spacer(modifier = Modifier.height(baseSpacing))
        ConvertFromValueTextField(
            modifier = Modifier.fillMaxWidth(),
            value = convertFromValue,
            onValueChange = onConvertFromValueChange,
            label = { Text(stringResource(R.string.enter_value_label)) }
        )
        Spacer(modifier = Modifier.height(baseSpacing))
        ConvertButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onConvertClick
        )
        Spacer(modifier = Modifier.height(baseSpacing))
        ConvertedValueDisplay(
            modifier = Modifier.fillMaxWidth(),
            convertedValue = convertToValue,
            convertedUnit = convertToUnit
        )
    }
}

@Composable
private fun UnitsSelection(
    modifier: Modifier = Modifier,
    convertFromQuantityType: QuantityType,
    convertFromUnit: QuantityUnit?,
    convertToQuantityType: QuantityType,
    convertToUnit: QuantityUnit?,
    onConvertFromUnitChange: (unit: QuantityUnit) -> Unit,
    onConvertToUnitChange: (unit: QuantityUnit) -> Unit,
    onSwapUnits: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        UnitSelector(
            modifier = Modifier.weight(1f),
            quantityType = convertFromQuantityType,
            selectedUnit = convertFromUnit,
            onUnitSelected = onConvertFromUnitChange
        )
        Spacer(modifier = Modifier.padding(baseSpacing))
        SwapConversionUnits(
            onSwapUnits = onSwapUnits
        )
        Spacer(modifier = Modifier.padding(baseSpacing))
        UnitSelector(
            modifier = Modifier.weight(1f), // Unit selectors share rest of space
            quantityType = convertToQuantityType,
            selectedUnit = convertToUnit,
            onUnitSelected = onConvertToUnitChange
        )
    }
}

@Composable
private fun ConvertFromValueTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (value: String) -> Unit,
    label: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Allow only numbers up to 6 decimal places
            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d{0,6}\$"))) {
                onValueChange(newValue)
            }
        },
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Show numeric keyboard
        modifier = modifier
    )
}

@Composable
private fun ConvertButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.convert_button_text))
    }
}

@Composable
private fun ConvertedValueDisplay(
    modifier: Modifier = Modifier,
    convertedValue: String,
    convertedUnit: QuantityUnit?
) {
    val context = LocalContext.current
    // Get the symbol for the converted unit
    val convertedUnitSymbol = remember(convertedUnit) {
        convertedUnit?.let { unit ->
            getUnitDisplaySymbol(context, unit)
        } ?: ""
    }

    Text(
        text = if (convertedValue.isNotEmpty() && convertedUnit != null) {
            stringResource(
                id = R.string.conversion_result_template,
                convertedValue,
                convertedUnitSymbol
            )
        } else {
            // Display a placeholder or empty string if no result yet
            ""
        },
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun SwapConversionUnits(
    onSwapUnits: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onSwapUnits,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.outline_swap_horiz_24),
            contentDescription = stringResource(R.string.swap_units_content_description)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitSelector(
    modifier: Modifier = Modifier,
    quantityType: QuantityType,
    selectedUnit: QuantityUnit?,
    onUnitSelected: (unit: QuantityUnit) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    // Determine the list of Unit instances based on QuantityType
    val units = remember(quantityType) {
        when (quantityType) {
            QuantityType.DISTANCE -> DistanceUnit.entries
            QuantityType.MASS -> MassUnit.entries
            QuantityType.TEMPERATURE -> TemperatureUnit.entries
        }
    }

    // Get the display name for the passed-in selected unit
    val selectedUnitName = remember(selectedUnit) {
        selectedUnit?.let { unit ->
            getUnitDisplayName(context, unit)
        } ?: "" // Default to empty string if no unit is selected
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedUnitName,
            onValueChange = { },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(
                    type = MenuAnchorType.PrimaryNotEditable
                )
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            units.forEach { unit ->
                val unitDisplayName = getUnitDisplayName(context, unit)
                DropdownMenuItem(
                    text = { Text(unitDisplayName) },
                    onClick = {
                        onUnitSelected(unit) // Call the callback with the selected Unit
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

private fun getUnitDisplayName(context: Context, unit: QuantityUnit): String {
    return when (unit) {
        is DistanceUnit -> distanceUnitName(context, unit)
        is MassUnit -> massUnitName(context, unit)
        is TemperatureUnit -> temperatureUnitName(context, unit)
    }
}

private fun getUnitDisplaySymbol(context: Context, unit: QuantityUnit): String {
    return when (unit) {
        is DistanceUnit -> distanceUnitSymbol(context, unit)
        is MassUnit -> massUnitSymbol(context, unit)
        is TemperatureUnit -> temperatureUnitSymbol(context, unit)
    }
}

@Preview(showBackground = true, name = "TwoWayConverter - Distance")
@Composable
fun TwoWayConverterDistancePreview() {
    TwoWayConverter(
        convertFromQuantityType = QuantityType.DISTANCE,
        convertFromUnit = DistanceUnit.METERS,
        convertFromValue = "10.0",
        convertToQuantityType = QuantityType.DISTANCE,
        convertToUnit = DistanceUnit.FEET,
        convertToValue = "32.8084",
        onConvertFromValueChange = {},
        onConvertFromUnitChange = {},
        onConvertToUnitChange = {},
        onSwapUnits = {},
        onConvertClick = {}
    )
}

@Preview(showBackground = true, name = "TwoWayConverter - Mass")
@Composable
fun TwoWayConverterMassPreview() {
    TwoWayConverter(
        convertFromQuantityType = QuantityType.MASS,
        convertFromUnit = MassUnit.KILOGRAMS,
        convertFromValue = "5.0",
        convertToQuantityType = QuantityType.MASS,
        convertToUnit = MassUnit.POUNDS,
        convertToValue = "11.0231",
        onConvertFromValueChange = {},
        onConvertFromUnitChange = {},
        onConvertToUnitChange = {},
        onSwapUnits = {},
        onConvertClick = {}
    )
}

@Preview(showBackground = true, name = "TwoWayConverter - Temperature")
@Composable
fun TwoWayConverterTemperaturePreview() {
    TwoWayConverter(
        convertFromQuantityType = QuantityType.TEMPERATURE,
        convertFromUnit = TemperatureUnit.CELSIUS,
        convertFromValue = "25.0",
        convertToQuantityType = QuantityType.TEMPERATURE,
        convertToUnit = TemperatureUnit.FAHRENHEIT,
        convertToValue = "77.0",
        onConvertFromValueChange = {},
        onConvertFromUnitChange = {},
        onConvertToUnitChange = {},
        onSwapUnits = {},
        onConvertClick = {}
    )
}

