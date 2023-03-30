package com.tomczyn.data

import android.app.Application
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class HealthDataRepository @Inject constructor(private val context: Application) {

    private val list: List<HealthData> = readCsv()
    private val cursor = list.iterator().withIndex()

    fun getData(): IndexedValue<HealthData> {
        return cursor.next()
    }

    fun hasNext(): Boolean {
        return cursor.hasNext()
    }

    private fun readCsv(): List<HealthData> {
        val healthDataList = mutableListOf<HealthData>()
        val inputStream = context.assets.open("heart_data.csv")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        bufferedReader.readLine() // skip the first line (header)
        var line: String? = bufferedReader.readLine()
        while (line != null) {
            val tokens = line.split(",")
            val healthData = HealthData(
                age = tokens[2].toInt(),
                gender = tokens[3],
                height = tokens[4].toInt(),
                weight = tokens[5].toFloat(),
                ap_hi = tokens[6].toInt(),
                ap_lo = tokens[7].toInt(),
                cholesterol = tokens[8].toInt(),
                gluc = tokens[9].toInt(),
                smoke = tokens[10].toInt(),
                alco = tokens[11].toInt(),
                active = tokens[12].toInt(),
                cardio = tokens[13].toInt()
            )
            healthDataList.add(healthData)
            line = bufferedReader.readLine()
        }
        bufferedReader.close()
        return healthDataList
    }
}
