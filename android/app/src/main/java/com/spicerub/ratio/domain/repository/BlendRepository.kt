package com.spicerub.ratio.domain.repository

import com.spicerub.ratio.domain.model.Blend
import kotlinx.coroutines.flow.StateFlow

interface BlendRepository {
    val blends: StateFlow<List<Blend>>
    fun save(blend: Blend)
    fun find(id: String): Blend?
    fun newBlendId(): String
    fun today(): Long
}
