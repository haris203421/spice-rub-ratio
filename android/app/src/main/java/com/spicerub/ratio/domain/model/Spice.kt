package com.spicerub.ratio.domain.model

enum class SpiceGroup { BASE, HEAT, SALT }

enum class SpiceForm { GROUND, SEED, FLAKE }

data class Spice(
    val id: String,
    val name: String,
    val group: SpiceGroup,
    val form: SpiceForm,
    val gramsPerTsp: Double,
    val colorHex: Long,
    val descriptor: String,
    val onHand: Boolean = false
)
