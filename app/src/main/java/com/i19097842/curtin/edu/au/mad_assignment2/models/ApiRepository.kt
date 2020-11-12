package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

private const val OPEN_WEATHER_API_KEY = "aa7e3507e21f96994c769f929761529e"
private const val OPEN_WEATHER_API = "https://api.openweathermap.org/data/2.5/weather"

/**
 * Provides access to various api's. Currently only the Open Weather api.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class ApiRepository {

    /**
     * Retrieves the current temperature in a given city
     * @param[city] The city name
     * @param[stateCode] The state or territory code the city is in
     * @param[countryCode] The country code the state is in
     * @return The current temperature
     */
    suspend fun fetchTemperature(
        city: String,
        stateCode: String,
        countryCode: String
    ) : Int? {
        // Build the api url with the query string
        val urlStr = Uri
            .parse(OPEN_WEATHER_API)
            .buildUpon()
            .appendQueryParameter("q", "$city,$stateCode,$countryCode")
            .appendQueryParameter("appid", OPEN_WEATHER_API_KEY)
            .appendQueryParameter("units", "metric")
            .build().toString()

        // Blocking network code
        return withContext(Dispatchers.IO) {
            try {
                // Make api request and read result
                val json = JSONObject(URL(urlStr).readText())

                // Get temperature from json.main.temp
                val main = json.getJSONObject("main")
                val temp = main.getString("temp")

                // Cast to double then integer before returning
                temp.toDouble().toInt()
            } catch (e: Exception) {
                // Any error causes null to be returned
                null
            }
        }
    }
}