package com.spicerub.ratio.presentation.pantry

import com.spicerub.ratio.domain.model.SpiceForm

enum class PantryFilter { ALL, ON_HAND, HEAT }

data class PantryRowUi(
    val spiceId: String,
    val name: String,
    val initial: String,
    val form: SpiceForm,
    val densityLabel: String,
    val colorHex: Long,
    val onHand: Boolean,
    val inBlend: Boolean
)

data class PantryUiState(
    val rows: List<PantryRowUi> = emptyList(),
    val filter: PantryFilter = PantryFilter.ALL,
    val totalCount: Int = 0
) {
    val isEmpty: Boolean get() = rows.isEmpty()
}

sealed interface PantryEvent {
    data object SpiceAdded : PantryEvent
}
