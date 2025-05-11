package com.marcel.cubymark

import android.app.Application
import com.marcel.cubymark.unitconversion.DistanceConverter
import com.marcel.cubymark.unitconversion.MassConverter
import com.marcel.cubymark.unitconversion.TemperatureConverter
import com.marcel.cubymark.unitconversion.data.UnitConversionRepository


/**
 * Custom Application class that serves as a basic Service Locator
 * for application-level dependencies.
 */
class CUByMarkApplication : Application() {
    /**
     * Provides access to the Service Locator for obtaining application dependencies.
     */
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer()
    }
}

interface AppContainer {
    val unitConversionRepository: UnitConversionRepository
}

class DefaultAppContainer : AppContainer {
    private val distanceConverter: DistanceConverter by lazy { DistanceConverter() }
    private val massConverter: MassConverter by lazy { MassConverter() }
    private val temperatureConverter: TemperatureConverter by lazy { TemperatureConverter() }

    override val unitConversionRepository: UnitConversionRepository by lazy {
        UnitConversionRepository(distanceConverter, massConverter, temperatureConverter)
    }
}