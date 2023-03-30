package com.tomczyn.ui.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val greetingRoute: String = "greeting_route"

fun NavGraphBuilder.mainGraph() {
    composable(route = greetingRoute) {
        MainScreen()
    }
}
