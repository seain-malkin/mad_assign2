package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.RuntimeException
import java.net.URL

private const val OPEN_WEATHER_API_KEY = "aa7e3507e21f96994c769f929761529e"
private const val OPEN_WEATHER_API = "https://api.openweathermap.org/data/2.5/weather"

class ApiRepository {

    suspend fun fetchTemperature(
        city: String,
        stateCode: String,
        countryCode: String,
        units: String = "metric"
    ) : Int? {
        val urlStr = Uri
            .parse(OPEN_WEATHER_API)
            .buildUpon()
            .appendQueryParameter("q", "$city,$stateCode,$countryCode")
            .appendQueryParameter("appid", OPEN_WEATHER_API_KEY)
            .appendQueryParameter("units", units)
            .build().toString()

        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject(URL(urlStr).readText())
                val main = json.getJSONObject("main")
                main.getString("temp").toDouble().toInt()
            } catch (e: RuntimeException) {
                null
            }
        }
    }
}