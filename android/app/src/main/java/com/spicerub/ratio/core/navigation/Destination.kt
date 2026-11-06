package com.spicerub.ratio.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val blendId: String) : Screen
    data object Dials : Screen
    data object Pantry : Screen
    data class GameOver(val blendId: String, val batchGrams: Int) : Screen
}
