package com.pragmadreams.redaktor.android.navigation

import com.pragmadreams.redaktor.android.base.Effect

sealed class NavigationEffect : Effect {

    data class Navigate(val screen: Screen): NavigationEffect()

    object NavigateUp : NavigationEffect()
}
