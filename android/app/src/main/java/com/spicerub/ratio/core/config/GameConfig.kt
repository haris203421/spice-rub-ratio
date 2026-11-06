package com.spicerub.ratio.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val DONUT_MORPH_MS = 220
    const val DIAL_SNAP_MS = 220
    const val SCREEN_FADE_IN_MS = 420
    const val SCREEN_FADE_OUT_MS = 300
    const val STAGGER_STEP_MS = 55
    const val PRESS_DAMPING = 0.55f
    const val PRESS_STIFFNESS = 420f
    const val ENTER_DAMPING = 0.60f
    const val ENTER_STIFFNESS = 260f

    const val PART_STEP = 0.5
    const val PART_MIN = 0.0f
    const val PART_MAX = 10.0f
    const val PART_STEPS = 19

    const val HEAT_STEP = 0.25
    const val HEAT_MIN = 0.25
    const val HEAT_MAX = 2.50
    const val SALT_STEP = 0.25
    const val SALT_MIN = 0.0
    const val SALT_MAX = 2.0

    const val BATCH_MIN_G = 40
    const val BATCH_MAX_G = 1000
    const val MEAT_RUB_RATIO = 0.025
    const val MEAT_REFERENCE_G = 1000

    const val SHELF_LIFE_GROUND_D = 180L
    const val SHELF_LIFE_WHOLE_D = 365L

    const val MIN_SPICES = 2
    const val SEED_PARTS = 1.0
}
