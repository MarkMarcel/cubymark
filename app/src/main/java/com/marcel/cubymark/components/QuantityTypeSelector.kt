package com.marcel.cubymark.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcel.cubymark.theme.baseSpacing
import com.marcel.cubymark.unitconversion.models.QuantityType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuantityTypeSelector(
    modifier: Modifier = Modifier,
    selectedQuantityType: QuantityType?,
    onQuantityTypeSelected: (unit: QuantityType) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    // Get the display name for the passed-in selected quantity type
    val selectedQuantityTypeName = remember(selectedQuantityType) {
        selectedQuantityType?.let { unit ->
            quantityTypeName(context, unit)
        } ?: "" // Default to empty string if no quantity type is selected
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedQuantityTypeName,
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
            QuantityType.entries.forEach { quantityType ->
                val unitDisplayName = quantityTypeName(context, quantityType)
                DropdownMenuItem(
                    text = { Text(unitDisplayName) },
                    onClick = {
                        onQuantityTypeSelected(quantityType) // Call the callback with the selected Unit
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "QuantityTypeSelector Preview")
@Composable
fun QuantityTypeSelectorPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(baseSpacing)
    ) {
        QuantityTypeSelector(
            selectedQuantityType = QuantityType.DISTANCE,
            onQuantityTypeSelected = {}
        )
    }
}


