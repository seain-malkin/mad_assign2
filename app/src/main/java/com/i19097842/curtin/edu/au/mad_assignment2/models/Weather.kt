package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.util.*

// Must be at least 30 seconds between each request
private const val REQUEST_LIMIT: Int = 30 * 1000

/**
 * Weather model. Is basically a wrapper for the api calls. Uses the scope from [ViewModel] and
 * calls the api using a non-blocking approach
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 * @property[api] The injected api
 */
class Weather(
    val context: Context,
    val api: ApiRepository
): ViewModel() {
    /**
     * Listener interface. Must be implemented by caller
     */
    interface WeatherListener {
        /**
         * Triggered when the temperature is updated
         * @param[temperature] The new temperature
         */
        fun onTemperatureChange(temperature: Int)
    }

    /** Reference to the caller listening for changes */
    var listener: WeatherListener

    /** @property[temperature] The last temperature fetched from the api request */
    var temperature: Int? = null

    /** Time the last api call was made. Used to limit the number of requests */
    private var lastRequestTime: Long = 0

    init {
        // Ensure the context implements the WeatherListener interface
        if (context is WeatherListener) {
            listener = context
        } else {
            throw RuntimeException("${context.toString()} must implement WeatherListener")
        }
    }

    /**
     * Updates the temperature property
     */
    fun updateTemperature() {
        // Only continue if sufficient time has passed since the last request
        val curTime = System.currentTimeMillis()
        if (curTime - REQUEST_LIMIT > lastRequestTime) {
            lastRequestTime = curTime

            // Non-blocking call
            viewModelScope.launch {
                val temp = api.fetchTemperature("Perth", "WA", "AU")
                temp?.let {
                    // Only update if temperature has changed
                    if (temperature != temp) {
                        temperature = temp
                        listener.onTemperatureChange(temp)
                    }
                }
            }
        }
    }
}