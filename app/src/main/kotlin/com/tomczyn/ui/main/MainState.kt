package com.tomczyn.ui.main

import com.tomczyn.data.HealthData

data class MainState(
    val data: HealthData,
    val currentPrediction: Boolean,
    val numberOfPredictions: Int,
    val numberOfCorrect: Int,
    val numberOfIncorrect: Int,
    val performance: Float,
)
