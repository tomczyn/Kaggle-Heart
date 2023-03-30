@file:OptIn(ExperimentalTime::class)

package com.tomczyn.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomczyn.data.HealthDataModel
import com.tomczyn.data.HealthDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@HiltViewModel
class MainViewModel @Inject constructor(
    private val healthRepository: HealthDataRepository,
    private val healthModel: HealthDataModel,
) : ViewModel() {

    private val _state: MutableStateFlow<MainState>
    val state: StateFlow<MainState> get() = _state

    init {
        val firstData = healthRepository.getData().value
        val firstPrediction = healthModel.predict(firstData)
        _state = MutableStateFlow(
            MainState(
                data = firstData,
                currentPrediction = firstPrediction,
                numberOfPredictions = 1,
                numberOfCorrect = if (firstPrediction == firstData.result) 1 else 0,
                numberOfIncorrect = if (firstPrediction == firstData.result) 0 else 1,
                performance = if (firstPrediction == firstData.result) 1f else 0f,
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            val time = measureTime {
                while (healthRepository.hasNext()) {
                    nextPrediction()
                }
            }
            Timber.d("Predicted in $time")
        }
    }

    fun predict() {
        nextPrediction()
    }

    private fun nextPrediction() {
        val indexedData = healthRepository.getData()
        val data = indexedData.value
        val index = indexedData.index
        val prediction = healthModel.predict(data)
        val currentState = _state.value
        val numberOfCorrect =
            if (prediction == data.result) currentState.numberOfCorrect + 1 else currentState.numberOfCorrect
        val numberOfIncorrect =
            if (prediction == data.result) currentState.numberOfIncorrect else currentState.numberOfIncorrect + 1
        val performance = (numberOfCorrect.toFloat() / index)
        _state.update {
            it.copy(
                data = data,
                currentPrediction = prediction,
                numberOfPredictions = it.numberOfPredictions + 1,
                numberOfCorrect = numberOfCorrect,
                numberOfIncorrect = numberOfIncorrect,
                performance = performance,
            )
        }
    }
}
