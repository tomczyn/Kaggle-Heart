package com.tomczyn.data

import android.app.Application
import android.content.res.AssetFileDescriptor
import org.json.JSONArray
import org.tensorflow.lite.Interpreter
import timber.log.Timber
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject


class HealthDataModel @Inject constructor(
    context: Application
) {

    private val interpreter: Interpreter
    private val scale = loadScaler(context, scaleName)
    private val mean = loadScaler(context, meanName)

    init {
        val assetFileDescriptor: AssetFileDescriptor = context.assets.openFd(modelName)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        val model: MappedByteBuffer =
            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        interpreter = Interpreter(model)
    }

    fun predict(data: HealthData): Boolean {
        val inputData = doubleArrayOf(
            data.age.toDouble(),
            if (data.gender == "male") 1.0 else 0.0,
            data.height.toDouble(),
            data.weight.toDouble(),
            data.ap_hi.toDouble(),
            data.ap_lo.toDouble(),
            data.cholesterol.toDouble(),
            data.gluc.toDouble(),
            data.smoke.toDouble(),
            data.alco.toDouble(),
            data.active.toDouble()
        )
        val scaledData = DoubleArray(inputData.size) { i ->
            (inputData[i] - mean[i]) / scale[i]
        }
        val input = ByteBuffer.allocateDirect(4 * 11).order(ByteOrder.nativeOrder())
        for (value in scaledData) {
            input.putFloat(value.toFloat())
        }
        val output = Array(1) { FloatArray(1) }
        interpreter.run(input, output)
        Timber.d("Value ${output[0][0]}")
        return output[0][0] > 0.5
    }

    private fun loadScaler(context: Application, name: String): DoubleArray {
        val json = context.assets.open(name).bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)
        val scale = DoubleArray(jsonArray.length()) { i ->
            jsonArray.getDouble(i)
        }
        return scale
    }

    companion object {
        private const val modelName: String = "heart_model.tflite"
        private const val scaleName: String = "scaler_scale.json"
        private const val meanName: String = "scaler_mean.json"
    }
}
