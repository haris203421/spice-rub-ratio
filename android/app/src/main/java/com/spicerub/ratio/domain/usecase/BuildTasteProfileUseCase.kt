package com.spicerub.ratio.domain.usecase

import com.spicerub.ratio.domain.model.Blend
import com.spicerub.ratio.domain.model.Spice
import com.spicerub.ratio.domain.model.SpiceGroup

class BuildTasteProfileUseCase {

    operator fun invoke(blend: Blend, spices: List<Spice>): String {
        val byId = spices.associateBy { it.id }
        val base = blend.items
            .mapNotNull { item -> byId[item.spiceId]?.let { it to item.parts } }
            .filter { it.first.group == SpiceGroup.BASE }
            .sortedByDescending { it.second }
            .take(2)
            .map { it.first.descriptor }
            .distinct()
        return (base + heatWord(blend.heatMultiplier)).joinToString(SEPARATOR)
    }

    fun heatWord(multiplier: Double): String = when {
        multiplier < 0.75 -> "MILD HEAT"
        multiplier < 1.5 -> "MEDIUM HEAT"
        multiplier < 2.0 -> "BIG HEAT"
        else -> "FIERY"
    }

    private companion object {
        const val SEPARATOR = " · "
    }
}
