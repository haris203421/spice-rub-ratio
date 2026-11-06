package com.spicerub.ratio.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.domain.model.Blend
import com.spicerub.ratio.domain.model.BlendItem
import com.spicerub.ratio.domain.model.MeasureUnit
import com.spicerub.ratio.domain.model.Spice
import com.spicerub.ratio.domain.model.SpiceGroup
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
import kotlin.math.roundToInt

class GameViewModel(
    private val blendRepository: BlendRepository,
    private val spiceRepository: SpiceRepository,
    private val draftStore: BlendDraftStore,
    private val scaleBlend: ScaleBlendUseCase,
    private val formatAmount: FormatAmountUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = uiState.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    init {
        combine(draftStore.draft, spiceRepository.pantry) { blend, spices ->
            build(blend, spices)
        }
            .onEach { uiState.value = it }
            .launchIn(viewModelScope)
    }

    fun setParts(spiceId: String, parts: Double) {
        draftStore.update { blend ->
            val items = blend.items.map {
                if (it.spiceId == spiceId) it.copy(parts = parts) else it
            }
            blend.copy(items = items)
        }
    }

    fun addSpice(spiceId: String) {
        draftStore.update { blend ->
            if (blend.items.any { it.spiceId == spiceId }) {
                blend
            } else {
                blend.copy(items = blend.items + BlendItem(spiceId, GameConfig.SEED_PARTS))
            }
        }
    }

    fun removeSpice(spiceId: String) {
        draftStore.update { blend ->
            blend.copy(items = blend.items.filterNot { it.spiceId == spiceId })
        }
    }

    fun setBatch(grams: Int) {
        val clamped = grams.coerceIn(GameConfig.BATCH_MIN_G, GameConfig.BATCH_MAX_G)
        draftStore.update { it.copy(batchGrams = clamped) }
    }

    fun setUnit(unit: MeasureUnit) {
        draftStore.update { it.copy(unit = unit) }
    }

    fun setName(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        draftStore.update { blend -> blend.copy(name = trimmed) }
    }

    fun setNotes(notes: String) {
        draftStore.update { it.copy(notes = notes.trim()) }
    }

    fun scaleAndLabel() {
        val blend = draftStore.current()
        if (!blend.isValid) return
        blendRepository.save(blend)
        viewModelScope.launch {
            eventChannel.send(GameEvent.Scaled(blend.id, blend.batchGrams))
        }
    }

    private fun build(blend: Blend, spices: List<Spice>): GameUiState {
        val byId = spices.associateBy { it.id }
        val scaled = scaleBlend(blend, spices)
        val amounts = scaled.lines.associateBy { it.spiceId }
        val rows = blend.items.mapNotNull { item ->
            val spice = byId[item.spiceId] ?: return@mapNotNull null
            SpiceRowUi(
                spiceId = spice.id,
                name = spice.name,
                parts = item.parts,
                partsLabel = formatAmount.formatParts(item.parts),
                amountLabel = amounts[spice.id]?.display.orEmpty(),
                colorHex = spice.colorHex,
                badge = when (spice.group) {
                    SpiceGroup.HEAT -> GroupBadge.HEAT
                    SpiceGroup.SALT -> GroupBadge.SALT
                    SpiceGroup.BASE -> null
                }
            )
        }
        val used = blend.items.map { it.spiceId }.toSet()
        val available = spices.filterNot { used.contains(it.id) }
            .map { SpiceChipUi(it.id, it.name, it.colorHex) }
        val meatGrams = (GameConfig.MEAT_REFERENCE_G * GameConfig.MEAT_RUB_RATIO).roundToInt()
        return GameUiState(
            blendId = blend.id,
            name = blend.name,
            notes = blend.notes,
            rows = rows,
            available = available,
            segments = scaleBlend.segments(blend, spices),
            totalPartsValue = formatAmount.formatParts(scaled.totalParts),
            batchGrams = blend.batchGrams,
            batchLabel = blend.batchGrams.toString(),
            presets = listOf(
                BatchPreset(50, false),
                BatchPreset(100, false),
                BatchPreset(250, false),
                BatchPreset(500, false),
                BatchPreset(meatGrams, true)
            ),
            unit = blend.unit,
            heatValue = formatAmount.formatMultiplier(blend.heatMultiplier),
            saltValue = formatAmount.formatMultiplier(blend.saltMultiplier),
            yieldValue = formatAmount.formatGrams(blend.batchGrams.toDouble()),
            donutDescription = rows.joinToString(", ") { it.name + " " + it.partsLabel + " parts" },
            canScale = blend.isValid && blend.items.size >= GameConfig.MIN_SPICES
        )
    }
}
