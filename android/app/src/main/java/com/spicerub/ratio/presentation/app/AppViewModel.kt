package com.spicerub.ratio.presentation.app

import androidx.lifecycle.ViewModel
import com.spicerub.ratio.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    private val screenState = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = screenState.asStateFlow()

    private val canGoBackState = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = canGoBackState.asStateFlow()

    fun navigate(target: Screen) {
        val current = screenState.value
        if (current != Screen.Splash) backStack.add(current)
        screenState.value = target
        canGoBackState.value = backStack.isNotEmpty()
    }

    fun back() {
        if (backStack.isEmpty()) return
        screenState.value = backStack.removeAt(backStack.lastIndex)
        canGoBackState.value = backStack.isNotEmpty()
    }

    fun resetToMenu() {
        backStack.clear()
        screenState.value = Screen.Menu
        canGoBackState.value = false
    }
}
