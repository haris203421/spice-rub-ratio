package com.spicerub.ratio.domain.usecase

import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.domain.model.Blend
import com.spicerub.ratio.domain.model.RatioSegment
import com.spicerub.ratio.domain.model.ScaledBlend
import com.spicerub.ratio.domain.model.ScaledLine
import com.spicerub.ratio.domain.model.Spice
import com.spicerub.ratio.domain.model.SpiceForm
import com.spicerub.ratio.domain.model.SpiceGroup
import kotlin.math.roundToInt

class ScaleBlendUseCase(
    private val formatAmount: FormatAmountUseCase,
    private val buildProfile: BuildTasteProfileUseCase
) {

    operator fun invoke(blend: Blend, spices: List<Spice>): ScaledBlend {
        val byId = spices.associateBy { it.id }
        val resolved = blend.items.mapNotNull { item -> byId[item.spiceId]?.let { it to item.parts } }
        val effective = resolved.map { pair -> pair.first to effectiveParts(pair.first, pair.second, blend) }
        val total = effective.sumOf { it.second }
        val lines = effective.map { pair ->
            val spice = pair.first
            val grams = if (total <= 0.0) 0.0 else blend.batchGrams * pair.second / total
            val tsp = if (spice.gramsPerTsp <= 0.0) 0.0 else grams / spice.gramsPerTsp
            ScaledLine(
                spiceId = spice.id,
                name = spice.name,
                colorHex = spice.colorHex,
                grams = grams,
                tsp = tsp,
                display = formatAmount(grams, spice.gramsPerTsp, blend.unit)
            )
        }
        val shelfLife = if (resolved.any { it.first.form == SpiceForm.GROUND }) {
            GameConfig.SHELF_LIFE_GROUND_D
        } else {
            GameConfig.SHELF_LIFE_WHOLE_D
        }
        return ScaledBlend(
            lines = lines,
            totalGrams = blend.batchGrams,
            totalParts = roundHalf(blend.items.sumOf { it.parts }),
            profile = buildProfile(blend, spices),
            bestBeforeEpochDay = blend.createdEpochDay + shelfLife,
            createdEpochDay = blend.createdEpochDay,
            spiceCount = blend.items.size,
            heatMultiplier = blend.heatMultiplier
        )
    }

    fun segments(blend: Blend, spices: List<Spice>): List<RatioSegment> {
        val byId = spices.associateBy { it.id }
        return blend.items.mapNotNull { item ->
            byId[item.spiceId]?.let { spice ->
                RatioSegment(
                    id = spice.id,
                    weight = effectiveParts(spice, item.parts, blend).toFloat(),
                    colorHex = spice.colorHex
                )
            }
        }
    }

    private fun effectiveParts(spice: Spice, parts: Double, blend: Blend): Double = when (spice.group) {
        SpiceGroup.HEAT -> parts * blend.heatMultiplier
        SpiceGroup.SALT -> parts * blend.saltMultiplier
        SpiceGroup.BASE -> parts
    }

    private fun roundHalf(value: Double): Double = (value * 2.0).roundToInt() / 2.0
}
