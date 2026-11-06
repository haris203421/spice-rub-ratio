package com.spicerub.ratio.presentation.dials

import com.spicerub.ratio.domain.model.RatioSegment

enum class HeatWord { MILD, MEDIUM, HOT, FIERY }

enum class SaltWord { LOW, BALANCED, SALTY }

data class DialsUiState(
    val heat: Double = 1.0,
    val salt: Double = 1.0,
    val heatValue: String = "",
    val saltValue: String = "",
    val heatWord: HeatWord = HeatWord.MEDIUM,
    val saltWord: SaltWord = SaltWord.BALANCED,
    val heatIconScale: Float = 0.5f,
    val saltIconScale: Float = 0.5f,
    val segments: List<RatioSegment> = emptyList()
)
