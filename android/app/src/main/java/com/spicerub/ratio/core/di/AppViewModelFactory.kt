package com.spicerub.ratio.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spicerub.ratio.presentation.app.AppViewModel
import com.spicerub.ratio.presentation.dials.DialsViewModel
import com.spicerub.ratio.presentation.game.GameViewModel
import com.spicerub.ratio.presentation.gameover.GameOverViewModel
import com.spicerub.ratio.presentation.menu.MenuViewModel
import com.spicerub.ratio.presentation.pantry.PantryViewModel
import com.spicerub.ratio.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                blendRepository = ServiceLocator.blendRepository,
                spiceRepository = ServiceLocator.spiceRepository,
                draftStore = ServiceLocator.draftStore,
                scaleBlend = ServiceLocator.scaleBlend,
                formatAmount = ServiceLocator.formatAmount
            )
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                blendRepository = ServiceLocator.blendRepository,
                spiceRepository = ServiceLocator.spiceRepository,
                draftStore = ServiceLocator.draftStore,
                scaleBlend = ServiceLocator.scaleBlend,
                formatAmount = ServiceLocator.formatAmount
            )
            modelClass.isAssignableFrom(DialsViewModel::class.java) -> DialsViewModel(
                spiceRepository = ServiceLocator.spiceRepository,
                draftStore = ServiceLocator.draftStore,
                scaleBlend = ServiceLocator.scaleBlend,
                formatAmount = ServiceLocator.formatAmount
            )
            modelClass.isAssignableFrom(PantryViewModel::class.java) -> PantryViewModel(
                spiceRepository = ServiceLocator.spiceRepository,
                draftStore = ServiceLocator.draftStore,
                formatAmount = ServiceLocator.formatAmount
            )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                blendRepository = ServiceLocator.blendRepository,
                spiceRepository = ServiceLocator.spiceRepository,
                draftStore = ServiceLocator.draftStore,
                scaleBlend = ServiceLocator.scaleBlend,
                formatAmount = ServiceLocator.formatAmount
            )
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
