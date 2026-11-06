package com.spicerub.ratio.presentation.dials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.domain.model.Blend
import com.spicerub.ratio.domain.model.Spice
import com.spicerub.ratio.domain.repository.SpiceRepository
import com.spicerub.ratio.domain.session.BlendDraftStore
import com.spicerub.ratio.domain.usecase.FormatAmountUseCase
import com.spicerub.ratio.domain.usecase.ScaleBlendUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DialsViewModel(
    private val spiceRepository: SpiceRepository,
    private val draftStore: BlendDraftStore,
    private val scaleBlend: ScaleBlendUseCase,
    private val formatAmount: FormatAmountUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(DialsUiState())
    val state: StateFlow<DialsUiState> = uiState.asStateFlow()

    init {
        combine(draftStore.draft, spiceRepository.pantry) { blend, spices ->
            build(blend, spices)
        }
            .onEach { uiState.value = it }
            .launchIn(viewModelScope)
    }

    fun setHeat(value: Double) {
        val clamped = value.coerceIn(GameConfig.HEAT_MIN, GameConfig.HEAT_MAX)
        draftStore.update { it.copy(heatMultiplier = clamped) }
    }

    fun setSalt(value: Double) {
        val clamped = value.coerceIn(GameConfig.SALT_MIN, GameConfig.SALT_MAX)
        draftStore.update { it.copy(saltMultiplier = clamped) }
    }

    private fun build(blend: Blend, spices: List<Spice>): DialsUiState = DialsUiState(
        heat = blend.heatMultiplier,
        salt = blend.saltMultiplier,
        heatValue = formatAmount.formatMultiplier(blend.heatMultiplier),
        saltValue = formatAmount.formatMultiplier(blend.saltMultiplier),
        heatWord = when {
            blend.heatMultiplier < 0.75 -> HeatWord.MILD
            blend.heatMultiplier < 1.5 -> HeatWord.MEDIUM
            blend.heatMultiplier < 2.0 -> HeatWord.HOT
            else -> HeatWord.FIERY
        },
        saltWord = when {
            blend.saltMultiplier < 0.75 -> SaltWord.LOW
            blend.saltMultiplier < 1.5 -> SaltWord.BALANCED
            else -> SaltWord.SALTY
        },
        heatIconScale = (blend.heatMultiplier / GameConfig.HEAT_MAX).toFloat().coerceIn(0f, 1f),
        saltIconScale = (blend.saltMultiplier / GameConfig.SALT_MAX).toFloat().coerceIn(0f, 1f),
        segments = scaleBlend.segments(blend, spices)
    )
}
