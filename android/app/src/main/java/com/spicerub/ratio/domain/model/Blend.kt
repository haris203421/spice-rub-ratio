package com.spicerub.ratio.domain.model

enum class MeasureUnit { GRAMS, SPOONS }

data class BlendItem(val spiceId: String, val parts: Double)

data class Blend(
    val id: String,
    val name: String,
    val items: List<BlendItem>,
    val heatMultiplier: Double,
    val saltMultiplier: Double,
    val batchGrams: Int,
    val unit: MeasureUnit,
    val notes: String,
    val createdEpochDay: Long
) {
    val isValid: Boolean
        get() = items.size >= 2 && items.sumOf { it.parts } > 0.0

    fun partsOf(spiceId: String): Double = items.firstOrNull { it.spiceId == spiceId }?.parts ?: 0.0

    companion object {
        val Blank = Blend(
            id = "",
            name = "",
            items = emptyList(),
            heatMultiplier = 1.0,
            saltMultiplier = 1.0,
            batchGrams = 250,
            unit = MeasureUnit.GRAMS,
            notes = "",
            createdEpochDay = 0L
        )
    }
}
