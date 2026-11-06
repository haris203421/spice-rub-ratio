package com.spicerub.ratio.data.local

import android.content.Context
import com.spicerub.ratio.domain.model.Blend
import com.spicerub.ratio.domain.model.BlendItem
import com.spicerub.ratio.domain.model.MeasureUnit

class BlendPreferences(context: Context) {

    private val prefs = context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun isSeeded(): Boolean = prefs.getBoolean(KEY_SEEDED, false)

    fun markSeeded() {
        prefs.edit().putBoolean(KEY_SEEDED, true).apply()
    }

    fun readBlends(): List<Blend> {
        val raw = prefs.getString(KEY_BLENDS, "").orEmpty()
        if (raw.isBlank()) return emptyList()
        return raw.split(RECORD_SEPARATOR).mapNotNull { decode(it) }
    }

    fun writeBlends(blends: List<Blend>) {
        val raw = blends.joinToString(RECORD_SEPARATOR) { encode(it) }
        prefs.edit().putString(KEY_BLENDS, raw).apply()
    }

    fun readOnHand(): Set<String> = prefs.getStringSet(KEY_ON_HAND, emptySet()).orEmpty().toSet()

    fun writeOnHand(ids: Set<String>) {
        prefs.edit().putStringSet(KEY_ON_HAND, ids).apply()
    }

    private fun encode(blend: Blend): String {
        val items = blend.items.joinToString(ITEM_SEPARATOR) { it.spiceId + PAIR_SEPARATOR + it.parts }
        return listOf(
            blend.id,
            clean(blend.name),
            blend.heatMultiplier.toString(),
            blend.saltMultiplier.toString(),
            blend.batchGrams.toString(),
            blend.unit.name,
            clean(blend.notes),
            blend.createdEpochDay.toString(),
            items
        ).joinToString(FIELD_SEPARATOR)
    }

    private fun decode(record: String): Blend? {
        val parts = record.split(FIELD_SEPARATOR)
        if (parts.size < FIELD_COUNT) return null
        val items = parts[8].split(ITEM_SEPARATOR).mapNotNull { entry ->
            val pair = entry.split(PAIR_SEPARATOR)
            val amount = pair.getOrNull(1)?.toDoubleOrNull()
            if (pair.size == 2 && amount != null) BlendItem(pair[0], amount) else null
        }
        if (items.isEmpty()) return null
        return Blend(
            id = parts[0],
            name = parts[1],
            items = items,
            heatMultiplier = parts[2].toDoubleOrNull() ?: 1.0,
            saltMultiplier = parts[3].toDoubleOrNull() ?: 1.0,
            batchGrams = parts[4].toIntOrNull() ?: 250,
            unit = runCatching { MeasureUnit.valueOf(parts[5]) }.getOrDefault(MeasureUnit.GRAMS),
            notes = parts[6],
            createdEpochDay = parts[7].toLongOrNull() ?: 0L
        )
    }

    private fun clean(value: String): String =
        value.replace(FIELD_SEPARATOR, " ").replace(RECORD_SEPARATOR, " ").trim()

    private companion object {
        const val STORE = "spice_rub_ratio_store"
        const val KEY_BLENDS = "blends"
        const val KEY_ON_HAND = "on_hand"
        const val KEY_SEEDED = "seeded"
        const val FIELD_SEPARATOR = "|"
        const val RECORD_SEPARATOR = "\n"
        const val ITEM_SEPARATOR = ","
        const val PAIR_SEPARATOR = ":"
        const val FIELD_COUNT = 9
    }
}
