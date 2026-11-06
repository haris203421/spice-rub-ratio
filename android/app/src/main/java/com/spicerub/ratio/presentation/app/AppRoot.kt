package com.spicerub.ratio.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spicerub.ratio.core.config.GameConfig
import com.spicerub.ratio.core.di.AppViewModelFactory
import com.spicerub.ratio.core.navigation.Screen
import com.spicerub.ratio.core.theme.BackgroundTop
import com.spicerub.ratio.presentation.dials.DialsRoute
import com.spicerub.ratio.presentation.game.GameRoute
import com.spicerub.ratio.presentation.gameover.GameOverRoute
import com.spicerub.ratio.presentation.menu.MenuRoute
import com.spicerub.ratio.presentation.pantry.PantryRoute
import com.spicerub.ratio.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) { appViewModel.back() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundTop)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                when (targetState) {
                    is Screen.GameOver -> (
                        scaleIn(
                            initialScale = 0.92f,
                            animationSpec = spring(
                                dampingRatio = GameConfig.ENTER_DAMPING,
                                stiffness = GameConfig.ENTER_STIFFNESS
                            )
                        ) + fadeIn(tween(GameConfig.SCREEN_FADE_OUT_MS))
                        ) togetherWith fadeOut(tween(220))

                    is Screen.Game -> (
                        slideInHorizontally(
                            animationSpec = tween(360),
                            initialOffsetX = { full -> full / 4 }
                        ) + fadeIn(tween(280))
                        ) togetherWith fadeOut(tween(220))

                    else -> fadeIn(tween(GameConfig.SCREEN_FADE_IN_MS)) togetherWith
                        fadeOut(tween(GameConfig.SCREEN_FADE_OUT_MS))
                }
            },
            label = "screenTransition"
        ) { target ->
            when (target) {
                Screen.Splash -> SplashRoute(onFinished = { appViewModel.resetToMenu() })

                Screen.Menu -> MenuRoute(
                    onOpenBuilder = { blendId -> appViewModel.navigate(Screen.Game(blendId)) },
                    onOpenDials = { appViewModel.navigate(Screen.Dials) },
                    onOpenPantry = { appViewModel.navigate(Screen.Pantry) }
                )

                is Screen.Game -> GameRoute(
                    onBack = { appViewModel.back() },
                    onOpenDials = { appViewModel.navigate(Screen.Dials) },
                    onOpenPantry = { appViewModel.navigate(Screen.Pantry) },
                    onScaled = { blendId, batchGrams ->
                        appViewModel.navigate(Screen.GameOver(blendId, batchGrams))
                    }
                )

                Screen.Dials -> DialsRoute(onBack = { appViewModel.back() })

                Screen.Pantry -> PantryRoute(onBack = { appViewModel.back() })

                is Screen.GameOver -> GameOverRoute(
                    blendId = target.blendId,
                    batchGrams = target.batchGrams,
                    onBlendAgain = { blendId -> appViewModel.navigate(Screen.Game(blendId)) },
                    onMenu = { appViewModel.resetToMenu() }
                )
            }
        }
    }
}
