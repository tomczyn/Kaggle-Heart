package com.tomczyn.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomczyn.data.HealthData
import com.tomczyn.ui.common.AppScreen
import com.tomczyn.ui.common.theme.KaggleHeartTheme

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    AppScreen {
        ParticipantData(healthData = state.data)
        Spacer(Modifier.height(16.dp))
        Prediction(prediction = state.currentPrediction, healthData = state.data)
        Spacer(Modifier.height(16.dp))
        Summary(
            predictedQuantity = state.numberOfPredictions,
            correctQuantity = state.numberOfCorrect,
            incorrectQuantity = state.numberOfIncorrect,
            average = state.performance
        )
    }
}

@Composable
fun ParticipantData(healthData: HealthData) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Age: ${healthData.age}")
        Text(text = "Gender: ${healthData.gender}")
        Text(text = "Height: ${healthData.height} cm")
        Text(text = "Weight: ${healthData.weight} kg")
        Text(text = "Systolic blood pressure: ${healthData.ap_hi}")
        Text(text = "Diastolic blood pressure: ${healthData.ap_lo}")
        Text(text = "Cholesterol level (mg/dL): ${healthData.cholesterol * 20}")
        Text(text = "Glucose level (mmol/L): ${healthData.gluc}")
        Text(text = "Smokes: ${if (healthData.smoke == 1) "Yes" else "No"}")
        Text(text = "Drinks alcohol: ${if (healthData.alco == 1) "Yes" else "No"}")
        Text(text = "Physically active: ${if (healthData.active == 1) "Yes" else "No"}")
        Text(text = "Cardiovascular disease: ${if (healthData.cardio == 1) "Yes" else "No"}")
    }
}

@Composable
fun Prediction(prediction: Boolean, healthData: HealthData) {
    Column {
        Text(
            text = "Prediction for cardiovascular disease: ${if (prediction) "Yes" else "No"}",
            fontWeight = FontWeight.Bold
        )
        if (prediction == (healthData.cardio == 1)) {
            Text("Correct", fontWeight = FontWeight.Bold, color = Color.Green)
        } else {
            Text("Incorrect", fontWeight = FontWeight.Bold, color = Color.Red)
        }
    }
}

@Composable
fun Summary(predictedQuantity: Int, correctQuantity: Int, incorrectQuantity: Int, average: Float) {
    Column {
        Text(text = "Predicted $predictedQuantity cases")
        Text(text = "Correct predictions: $correctQuantity")
        Text(text = "Incorrect predictions: $incorrectQuantity")
        Text(text = "Performance $average")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KaggleHeartTheme { MainScreen() }
}
