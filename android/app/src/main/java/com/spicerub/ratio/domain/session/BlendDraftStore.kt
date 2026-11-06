package com.spicerub.ratio.domain.session

import com.spicerub.ratio.domain.model.Blend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BlendDraftStore {

    private val state = MutableStateFlow(Blend.Blank)
    val draft: StateFlow<Blend> = state.asStateFlow()

    fun load(blend: Blend) {
        state.value = blend
    }

    fun update(transform: (Blend) -> Blend) {
        state.value = transform(state.value)
    }

    fun current(): Blend = state.value
}
