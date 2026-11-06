package com.spicerub.ratio.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spicerub.ratio.domain.model.Blend
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

class GameOverViewModel(
    private val blendRepository: BlendRepository,
    private val spiceRepository: SpiceRepository,
    private val draftStore: BlendDraftStore,
    private val scaleBlend: ScaleBlendUseCase,
    private val formatAmount: FormatAmountUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = uiState.asStateFlow()

    private val request = MutableStateFlow(LabelRequest())

    private val eventChannel = Channel<GameOverEvent>(Channel.BUFFERED)
    val events: Flow<GameOverEvent> = eventChannel.receiveAsFlow()

    init {
        combine(request, blendRepository.blends, spiceRepository.pantry) { target, blends, spices ->
            build(target, blends, spices)
        }
            .onEach { uiState.value = it }
            .launchIn(viewModelScope)
    }

    fun load(blendId: String, batchGrams: Int) {
        request.value = LabelRequest(blendId, batchGrams)
    }

    fun blendAgain() {
        val blendId = uiState.value.blendId
        val blend = blendRepository.find(blendId) ?: return
        draftStore.load(blend)
        viewModelScope.launch { eventChannel.send(GameOverEvent.OpenBuilder(blendId)) }
    }

    private fun build(
        target: LabelRequest,
        blends: List<Blend>,
        spices: List<Spice>
    ): GameOverUiState {
        val blend = blends.firstOrNull { it.id == target.blendId } ?: return GameOverUiState()
        val scaled = scaleBlend(blend, spices)
        val lines = scaled.lines.map { line ->
            LabelLineUi(
                spiceId = line.spiceId,
                name = line.name,
                amount = line.display,
                colorHex = line.colorHex
            )
        }
        val batch = if (target.batchGrams > 0) target.batchGrams else scaled.totalGrams
        return GameOverUiState(
            blendId = blend.id,
            blendName = blend.name,
            profile = scaled.profile,
            lines = lines,
            segments = scaleBlend.segments(blend, spices),
            batchLabel = formatAmount.formatGrams(batch.toDouble()),
            spiceCountLabel = scaled.spiceCount.toString(),
            partsLabel = formatAmount.formatParts(scaled.totalParts),
            shelfDays = (scaled.bestBeforeEpochDay - blendRepository.today())
                .coerceAtLeast(0L)
                .toInt(),
            donutDescription = lines.joinToString(", ") { it.name + " " + it.amount }
        )
    }

    private data class LabelRequest(
        val blendId: String = "",
        val batchGrams: Int = 0
    )
}
