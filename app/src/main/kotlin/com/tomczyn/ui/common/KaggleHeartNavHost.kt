package com.tomczyn.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tomczyn.ui.main.mainGraph
import com.tomczyn.ui.main.greetingRoute

@Composable
fun KaggleHeartNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = greetingRoute,
    ) {
        mainGraph()
    }
}

