package com.spicerub.ratio.domain.model

data class ScaledLine(
    val spiceId: String,
    val name: String,
    val colorHex: Long,
    val grams: Double,
    val tsp: Double,
    val display: String
)

data class ScaledBlend(
    val lines: List<ScaledLine>,
    val totalGrams: Int,
    val totalParts: Double,
    val profile: String,
    val bestBeforeEpochDay: Long,
    val createdEpochDay: Long,
    val spiceCount: Int,
    val heatMultiplier: Double
)

data class RatioSegment(
    val id: String,
    val weight: Float,
    val colorHex: Long
)
