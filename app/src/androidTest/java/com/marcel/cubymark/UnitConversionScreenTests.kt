package com.marcel.cubymark

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marcel.cubymark.components.DISPLAYED_MESSAGE_TEST_TAG
import com.marcel.cubymark.components.DISPLAYED_RESULT_TEST_TAG
import com.marcel.cubymark.components.DISPLAYED_VALUE_TO_CONVERT_TEST_TAG
import com.marcel.cubymark.components.QUANTITY_TYPE_SELECTOR_TEST_TAG
import com.marcel.cubymark.unitconversion.DistanceConverter
import com.marcel.cubymark.unitconversion.MassConverter
import com.marcel.cubymark.unitconversion.TemperatureConverter
import com.marcel.cubymark.unitconversion.data.UnitConversionRepository
import com.marcel.cubymark.unitconversion.ui.UnitConversionScreen
import com.marcel.cubymark.unitconversion.ui.UnitConversionScreenViewModel
import org.junit.Rule
import org.junit.Test


class UnitConversionScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val distanceConverter = DistanceConverter()
    private val massConverter = MassConverter()
    private val temperatureConverter = TemperatureConverter()

    private val unitConversionRepository = UnitConversionRepository(
        distanceConverter,
        massConverter,
        temperatureConverter
    )

    val testUnitConversionViewModelFactory: ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                UnitConversionScreenViewModel(unitConversionRepository)
            }
        }

    @Test
    fun givenExistingSelectionsDisplayedResultWhenUserChangesQuantityTypeThenConverterResets() {
        composeTestRule.setContent {
            // Provide the ViewModel using the test factory
            val viewModel =
                viewModel<UnitConversionScreenViewModel>(factory = testUnitConversionViewModelFactory)
            viewModel.onConvertFromValueChange("100")
            viewModel.onConvertClicked()

            // Render the screen composable
            UnitConversionScreen(
                viewModel = viewModel,
                testTags = mapOf(
                    DISPLAYED_MESSAGE_TEST_TAG to DISPLAYED_MESSAGE_TEST_TAG,
                    DISPLAYED_RESULT_TEST_TAG to DISPLAYED_RESULT_TEST_TAG,
                    DISPLAYED_VALUE_TO_CONVERT_TEST_TAG to DISPLAYED_VALUE_TO_CONVERT_TEST_TAG,
                    QUANTITY_TYPE_SELECTOR_TEST_TAG to QUANTITY_TYPE_SELECTOR_TEST_TAG,
                )
            )
        }

        // Interact with the Quantity Type Selector to change the quantity type
        val quantityTypeSelector = composeTestRule.onNodeWithTag(QUANTITY_TYPE_SELECTOR_TEST_TAG)

        // Click the selector to expand the dropdown menu
        quantityTypeSelector.performClick()

        val lastQuantityTypeDisplayName =
            "Temperature"

        // Find and click the text node for the last quantity type in the dropdown
        composeTestRule.onNode(
            hasText(lastQuantityTypeDisplayName, ignoreCase = true),
            useUnmergedTree = true
        )
            .performClick()


        // 3. Assert that the converter has been reset
        composeTestRule.onNodeWithTag(DISPLAYED_MESSAGE_TEST_TAG)
            .assertTextEquals("")
        composeTestRule.onNodeWithTag(DISPLAYED_RESULT_TEST_TAG)
            .assertTextEquals("")
        composeTestRule.onNodeWithTag(DISPLAYED_VALUE_TO_CONVERT_TEST_TAG)
            .assert(
                SemanticsMatcher.expectValue(SemanticsProperties.EditableText, AnnotatedString(""))
            )
    }
}