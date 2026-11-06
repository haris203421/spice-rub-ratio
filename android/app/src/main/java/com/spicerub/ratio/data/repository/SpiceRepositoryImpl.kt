package com.spicerub.ratio.data.repository

import com.spicerub.ratio.data.local.BlendPreferences
import com.spicerub.ratio.data.sample.SampleData
import com.spicerub.ratio.domain.model.Spice
import com.spicerub.ratio.domain.repository.SpiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SpiceRepositoryImpl(private val preferences: BlendPreferences) : SpiceRepository {

    private val state = MutableStateFlow(build(preferences.readOnHand()))

    override val pantry: StateFlow<List<Spice>> = state.asStateFlow()

    override fun find(id: String): Spice? = state.value.firstOrNull { it.id == id }

    override fun toggleOnHand(id: String) {
        val current = state.value.filter { it.onHand }.map { it.id }.toMutableSet()
        if (!current.add(id)) current.remove(id)
        preferences.writeOnHand(current)
        state.value = build(current)
    }

    private fun build(onHand: Set<String>): List<Spice> =
        SampleData.spices.map { it.copy(onHand = onHand.contains(it.id)) }
}
