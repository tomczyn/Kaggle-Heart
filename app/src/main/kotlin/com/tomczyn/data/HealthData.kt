package com.tomczyn.data

data class HealthData(
    val age: Int,
    val gender: String,
    val height: Int,
    val weight: Float,
    val ap_hi: Int,
    val ap_lo: Int,
    val cholesterol: Int,
    val gluc: Int,
    val smoke: Int,
    val alco: Int,
    val active: Int,
    val cardio: Int
) {
    val result: Boolean
        get() = cardio == 1
}