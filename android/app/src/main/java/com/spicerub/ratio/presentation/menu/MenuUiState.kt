package com.spicerub.ratio.presentation.menu

import com.spicerub.ratio.domain.model.RatioSegment

data class BlendCardUi(
    val id: String,
    val name: String,
    val profile: String,
    val spineColorHex: Long,
    val segments: List<RatioSegment>,
    val description: String
)

data class MenuUiState(
    val blends: List<BlendCardUi> = emptyList(),
    val blendCount: Int = 0,
    val spiceCount: Int = 0,
    val onHandCount: Int = 0
) {
    val isEmpty: Boolean get() = blends.isEmpty()
    val showStats: Boolean get() = blendCount > 0 && spiceCount > 0
}

sealed interface MenuEvent {
    data class OpenBuilder(val blendId: String) : MenuEvent
}
