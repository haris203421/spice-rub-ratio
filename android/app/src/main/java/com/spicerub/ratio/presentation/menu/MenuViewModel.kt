package com.spicerub.ratio.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spicerub.ratio.data.sample.SampleData
import com.spicerub.ratio.domain.model.Blend
import com.spicerub.ratio.domain.model.MeasureUnit
import com.spicerub.ratio.domain.model.Spice
import com.spicerub.ratio.domain.repository.BlendRepository
import com.spicerub.ratio.domain.repository.SpiceRepository
import com.spicerub.ratio.domain.session.BlendDraftStore
import com.spicerub.ratio.domain.usecase.FormatAmountUseCase
import com.spicerub.ratio.domain.usecase.ScaleBlendUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val blendRepository: BlendRepository,
    private val spiceRepository: SpiceRepository,
    private val draftStore: BlendDraftStore,
    private val scaleBlend: ScaleBlendUseCase,
    private val formatAmount: FormatAmountUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = uiState.asStateFlow()

    private val eventChannel = Channel<MenuEvent>(Channel.BUFFERED)
    val events: Flow<MenuEvent> = eventChannel.receiveAsFlow()

    init {
        combine(blendRepository.blends, spiceRepository.pantry) { blends, spices ->
            build(blends, spices)
        }
            .onEach { uiState.value = it }
            .launchIn(viewModelScope)
    }

    fun startNewBlend() {
        val blend = Blend(
            id = blendRepository.newBlendId(),
            name = SampleData.DEFAULT_BLEND_NAME,
            items = SampleData.seedItems(),
            heatMultiplier = 1.0,
            saltMultiplier = 1.0,
            batchGrams = 250,
            unit = MeasureUnit.GRAMS,
            notes = SampleData.DEFAULT_NOTES,
            createdEpochDay = blendRepository.today()
        )
        draftStore.load(blend)
        emit(blend.id)
    }

    fun openBlend(blendId: String) {
        val blend = blendRepository.find(blendId) ?: return
        draftStore.load(blend)
        emit(blend.id)
    }

    private fun emit(blendId: String) {
        viewModelScope.launch { eventChannel.send(MenuEvent.OpenBuilder(blendId)) }
    }

    private fun build(blends: List<Blend>, spices: List<Spice>): MenuUiState {
        val cards = blends.map { blend ->
            val segments = scaleBlend.segments(blend, spices)
            val scaled = scaleBlend(blend, spices)
            BlendCardUi(
                id = blend.id,
                name = blend.name,
                profile = scaled.profile,
                spineColorHex = segments.maxByOrNull { it.weight }?.colorHex ?: 0xFFB23A18,
                segments = segments,
                description = blend.name + ", " + blend.items.size + " spices, " + scaled.profile +
                    ", " + formatAmount.formatParts(scaled.totalParts) + " parts"
            )
        }
        return MenuUiState(
            blends = cards,
            blendCount = blends.size,
            spiceCount = spices.size,
            onHandCount = spices.count { it.onHand }
        )
    }
}
