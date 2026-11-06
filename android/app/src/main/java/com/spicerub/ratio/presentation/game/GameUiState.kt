package com.spicerub.ratio.presentation.game

import com.spicerub.ratio.domain.model.MeasureUnit
import com.spicerub.ratio.domain.model.RatioSegment

enum class GroupBadge { HEAT, SALT }

data class SpiceRowUi(
    val spiceId: String,
    val name: String,
    val parts: Double,
    val partsLabel: String,
    val amountLabel: String,
    val colorHex: Long,
    val badge: GroupBadge?
)

data class SpiceChipUi(
    val spiceId: String,
    val name: String,
    val colorHex: Long
)

data class BatchPreset(
    val grams: Int,
    val isMeat: Boolean
)

data class GameUiState(
    val blendId: String = "",
    val name: String = "",
    val notes: String = "",
    val rows: List<SpiceRowUi> = emptyList(),
    val available: List<SpiceChipUi> = emptyList(),
    val segments: List<RatioSegment> = emptyList(),
    val totalPartsValue: String = "",
    val batchGrams: Int = 250,
    val batchLabel: String = "",
    val presets: List<BatchPreset> = emptyList(),
    val unit: MeasureUnit = MeasureUnit.GRAMS,
    val heatValue: String = "",
    val saltValue: String = "",
    val yieldValue: String = "",
    val donutDescription: String = "",
    val canScale: Boolean = false
)

sealed interface GameEvent {
    data class Scaled(val blendId: String, val batchGrams: Int) : GameEvent
}
