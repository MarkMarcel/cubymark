package com.marcel.cubymark.unitconversion

import com.marcel.cubymark.unitconversion.models.Measurement
import com.marcel.cubymark.unitconversion.models.QuantityUnit

interface UnitConverter {
    suspend fun convert(
        measurement: Measurement,
        targetUnit: QuantityUnit
    ): Result<Measurement>
}