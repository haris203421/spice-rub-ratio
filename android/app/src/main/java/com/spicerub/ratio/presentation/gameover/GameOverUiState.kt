package com.spicerub.ratio.presentation.gameover

import com.spicerub.ratio.domain.model.RatioSegment

data class LabelLineUi(
    val spiceId: String,
    val name: String,
    val amount: String,
    val colorHex: Long
)

data class GameOverUiState(
    val blendId: String = "",
    val blendName: String = "",
    val profile: String = "",
    val lines: List<LabelLineUi> = emptyList(),
    val segments: List<RatioSegment> = emptyList(),
    val batchLabel: String = "",
    val spiceCountLabel: String = "",
    val partsLabel: String = "",
    val shelfDays: Int = 0,
    val donutDescription: String = ""
) {
    val isReady: Boolean get() = lines.isNotEmpty()
    val showStats: Boolean get() = lines.size >= 2
}

sealed interface GameOverEvent {
    data class OpenBuilder(val blendId: String) : GameOverEvent
}
