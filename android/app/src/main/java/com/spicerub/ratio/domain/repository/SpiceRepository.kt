package com.spicerub.ratio.domain.repository

import com.spicerub.ratio.domain.model.Spice
import kotlinx.coroutines.flow.StateFlow

interface SpiceRepository {
    val pantry: StateFlow<List<Spice>>
    fun find(id: String): Spice?
    fun toggleOnHand(id: String)
}
