package com.pragmadreams.redaktor.android.util


import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pragmadreams.redaktor.android.navigation.Screen

fun NavGraphBuilder.composable(
    route: Screen,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit,
) = composable(
    route = route.route,
    arguments = arguments,
    deepLinks = deepLinks,
    content = content,
)

inline fun NavGraphBuilder.navigation(
    startDestination: Screen,
    route: Screen,
    builder: NavGraphBuilder.() -> Unit,
): Unit {
    navigation(
        startDestination = startDestination.route,
        route = route.route,
        builder = builder,
    )
}