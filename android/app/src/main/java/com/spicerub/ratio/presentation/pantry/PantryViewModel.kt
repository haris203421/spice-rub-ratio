package com.spicerub.ratio.presentation.pantry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.domain.model.Blend
import com.spicerub.ratio.domain.model.BlendItem
import com.spicerub.ratio.domain.model.Spice
import com.spicerub.ratio.domain.model.SpiceGroup
import com.spicerub.ratio.domain.repository.SpiceRepository
import com.spicerub.ratio.domain.session.BlendDraftStore
import com.spicerub.ratio.domain.usecase.FormatAmountUseCase
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

class PantryViewModel(
    private val spiceRepository: SpiceRepository,
    private val draftStore: BlendDraftStore,
    private val formatAmount: FormatAmountUseCase
) : ViewModel() {

    private val filterState = MutableStateFlow(PantryFilter.ALL)

    private val uiState = MutableStateFlow(PantryUiState())
    val state: StateFlow<PantryUiState> = uiState.asStateFlow()

    private val eventChannel = Channel<PantryEvent>(Channel.BUFFERED)
    val events: Flow<PantryEvent> = eventChannel.receiveAsFlow()

    init {
        combine(spiceRepository.pantry, draftStore.draft, filterState) { spices, blend, filter ->
            build(spices, blend, filter)
        }
            .onEach { uiState.value = it }
            .launchIn(viewModelScope)
    }

    fun setFilter(filter: PantryFilter) {
        filterState.value = filter
    }

    fun toggleOnHand(spiceId: String) {
        spiceRepository.toggleOnHand(spiceId)
    }

    fun addToBlend(spiceId: String) {
        val blend = draftStore.current()
        if (blend.id.isEmpty() || blend.items.any { it.spiceId == spiceId }) return
        draftStore.update { it.copy(items = it.items + BlendItem(spiceId, GameConfig.SEED_PARTS)) }
        viewModelScope.launch { eventChannel.send(PantryEvent.SpiceAdded) }
    }

    private fun build(spices: List<Spice>, blend: Blend, filter: PantryFilter): PantryUiState {
        val used = blend.items.map { it.spiceId }.toSet()
        val filtered = when (filter) {
            PantryFilter.ALL -> spices
            PantryFilter.ON_HAND -> spices.filter { it.onHand }
            PantryFilter.HEAT -> spices.filter { it.group == SpiceGroup.HEAT }
        }
        return PantryUiState(
            rows = filtered.map { spice ->
                PantryRowUi(
                    spiceId = spice.id,
                    name = spice.name,
                    initial = spice.name.take(1).uppercase(),
                    form = spice.form,
                    densityLabel = formatAmount.formatParts(spice.gramsPerTsp),
                    colorHex = spice.colorHex,
                    onHand = spice.onHand,
                    inBlend = used.contains(spice.id)
                )
            },
            filter = filter,
            totalCount = spices.size
        )
    }
}
