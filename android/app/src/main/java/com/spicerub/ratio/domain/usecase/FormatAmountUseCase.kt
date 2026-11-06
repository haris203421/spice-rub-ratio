package com.spicerub.ratio.domain.usecase

import com.spicerub.ratio.domain.model.MeasureUnit
import kotlin.math.floor
import kotlin.math.roundToInt

class FormatAmountUseCase {

    operator fun invoke(grams: Double, gramsPerTsp: Double, unit: MeasureUnit): String =
        when (unit) {
            MeasureUnit.GRAMS -> formatGrams(grams)
            MeasureUnit.SPOONS -> formatSpoons(if (gramsPerTsp <= 0.0) 0.0 else grams / gramsPerTsp)
        }

    fun formatGrams(grams: Double): String =
        if (grams < 10.0) {
            val rounded = (grams * 10.0).roundToInt() / 10.0
            "$rounded g"
        } else {
            "${grams.roundToInt()} g"
        }

    fun formatParts(parts: Double): String {
        val rounded = (parts * 10.0).roundToInt() / 10.0
        val whole = rounded.toInt()
        return if (rounded - whole == 0.0) "$whole" else "$rounded"
    }

    fun formatMultiplier(value: Double): String {
        val rounded = (value * 100.0).roundToInt() / 100.0
        return "x$rounded"
    }

    private fun formatSpoons(tsp: Double): String {
        if (tsp <= 0.0) return "0 tsp"
        val tbsp = floor(tsp / 3.0).toInt()
        val rest = tsp - tbsp * 3.0
        val quarters = (rest * 4.0).roundToInt()
        val restText = quarterText(quarters)
        return when {
            tbsp > 0 && restText.isEmpty() -> "$tbsp tbsp"
            tbsp > 0 -> "$tbsp tbsp + $restText tsp"
            restText.isEmpty() -> "1/4 tsp"
            else -> "$restText tsp"
        }
    }

    private fun quarterText(quarters: Int): String {
        val whole = quarters / 4
        val frac = quarters % 4
        val fracText = when (frac) {
            1 -> "1/4"
            2 -> "1/2"
            3 -> "3/4"
            else -> ""
        }
        return when {
            whole > 0 && fracText.isEmpty() -> "$whole"
            whole > 0 -> "$whole $fracText"
            else -> fracText
        }
    }
}
