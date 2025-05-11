package com.marcel.cubymark.unitconversion.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcel.cubymark.R
import com.marcel.cubymark.components.QuantityTypeSelector
import com.marcel.cubymark.components.TwoWayConverter
import com.marcel.cubymark.theme.baseSpacing
import com.marcel.cubymark.theme.baseSpacingDiv2
import com.marcel.cubymark.unitconversion.models.QuantityType
import com.marcel.cubymark.unitconversion.models.QuantityUnit

@Composable
fun UnitConversionScreen(
    viewModel: UnitConversionScreenViewModel,
    testTags: Map<String, String> = emptyMap()
) {
    val state: UnitConversionScreenState by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            UnitConversionTopBar(
                Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            )
        },
    ) { padding ->
        UnitConversionScreenContent(
            modifier = Modifier
                .padding(padding),
            conversionQuantityType = state.conversionQuantityType,
            convertFromUnit = state.convertFromUnit,
            convertFromValue = state.convertFromValue,
            convertToUnit = state.convertToUnit,
            convertToValue = state.convertToValue,
            message = state.message,
            testTags = testTags,
            onConvertFromValueChange = viewModel::onConvertFromValueChange,
            onConvertFromUnitChange = viewModel::onConvertFromUnitChange,
            onConvertToUnitChange = viewModel::onConvertToUnitChange,
            onSwapUnits = viewModel::onSwapUnits,
            onConvertClick = viewModel::onConvertClicked,
            onQuantityTypeSelected = viewModel::onQuantityTypeSelected,
        )
    }
}

@Composable
private fun UnitConversionScreenContent(
    modifier: Modifier = Modifier,
    conversionQuantityType: QuantityType,
    convertFromUnit: QuantityUnit,
    convertFromValue: String,
    convertToUnit: QuantityUnit,
    convertToValue: String,
    message: String?,
    onConvertFromValueChange: (value: String) -> Unit,
    onConvertFromUnitChange: (unit: QuantityUnit) -> Unit,
    onConvertToUnitChange: (unit: QuantityUnit) -> Unit,
    onSwapUnits: () -> Unit,
    onConvertClick: () -> Unit,
    onQuantityTypeSelected: (unit: QuantityType) -> Unit,
    testTags: Map<String, String> = emptyMap()
) {
    Column(
        modifier = modifier
    ) {
        Instructions(
            Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(baseSpacing))
        LazyColumn(
            Modifier.weight(1f),
            contentPadding = PaddingValues(baseSpacing)
        ) {
            item {
                QuantityTypeSelector(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = baseSpacing),
                    selectedQuantityType = conversionQuantityType,
                    onQuantityTypeSelected = onQuantityTypeSelected,
                    testTags = testTags
                )
            }
            item {
                TwoWayConverter(
                    Modifier.fillMaxWidth(),
                    conversionQuantityType = conversionQuantityType,
                    convertFromUnit = convertFromUnit,
                    convertToUnit = convertToUnit,
                    convertFromValue = convertFromValue,
                    convertToValue = convertToValue,
                    message = message,
                    onConvertFromValueChange = onConvertFromValueChange,
                    onSwapUnits = onSwapUnits,
                    onConvertClick = onConvertClick,
                    onConvertFromUnitChange = onConvertFromUnitChange,
                    onConvertToUnitChange = onConvertToUnitChange,
                    testTags = testTags
                )
            }
        }
    }
}

@Composable
private fun UnitConversionTopBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier.padding(horizontal = baseSpacing)
    ) {
        Text(
            stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Composable
fun Instructions(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = baseSpacing)
    ) {
        Text(
            text = stringResource(id = R.string.step_1_instruction)
        )
        Spacer(modifier = Modifier.height(baseSpacingDiv2))
        Text(
            text = stringResource(id = R.string.step_2_instruction)
        )
        Spacer(modifier = Modifier.height(baseSpacingDiv2))
        Text(
            text = stringResource(id = R.string.step_3_instruction)
        )
    }
}

