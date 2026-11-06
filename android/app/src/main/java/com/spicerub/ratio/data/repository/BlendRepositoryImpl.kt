package com.spicerub.ratio.data.repository

import com.spicerub.ratio.data.local.BlendPreferences
import com.spicerub.ratio.data.sample.SampleData
import com.spicerub.ratio.domain.model.Blend
import com.spicerub.ratio.domain.repository.BlendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class BlendRepositoryImpl(private val preferences: BlendPreferences) : BlendRepository {

    private val state = MutableStateFlow(load())

    override val blends: StateFlow<List<Blend>> = state.asStateFlow()

    override fun save(blend: Blend) {
        val existing = state.value
        val index = existing.indexOfFirst { it.id == blend.id }
        val updated = if (index >= 0) {
            existing.toMutableList().apply { set(index, blend) }
        } else {
            existing + blend
        }
        state.value = updated
        preferences.writeBlends(updated)
    }

    override fun find(id: String): Blend? = state.value.firstOrNull { it.id == id }

    override fun newBlendId(): String = PREFIX + System.currentTimeMillis()

    override fun today(): Long = LocalDate.now().toEpochDay()

    private fun load(): List<Blend> {
        if (!preferences.isSeeded()) {
            val seeded = SampleData.starterBlends(LocalDate.now().toEpochDay())
            preferences.writeBlends(seeded)
            preferences.writeOnHand(SampleData.onHandOnFirstRun)
            preferences.markSeeded()
            return seeded
        }
        return preferences.readBlends()
    }

    private companion object {
        const val PREFIX = "blend_"
    }
}
