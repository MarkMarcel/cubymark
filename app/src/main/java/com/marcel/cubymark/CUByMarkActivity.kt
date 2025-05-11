package com.marcel.cubymark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.CubyMarkTheme
import com.marcel.cubymark.unitconversion.ui.UnitConversionScreen
import com.marcel.cubymark.unitconversion.ui.UnitConversionScreenViewModel

class CUByMarkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CubyMarkTheme {
                UnitConversionScreen(
                    Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
                    viewModel(factory = UnitConversionScreenViewModel.Factory)
                )
            }
        }
    }
}

