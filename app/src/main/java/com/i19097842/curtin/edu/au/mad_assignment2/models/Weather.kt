package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class Weather(
    val api: ApiRepository
): ViewModel() {
    fun getTemperature(): Int {
        viewModelScope.launch {
            val temp = api.fetchTemperature("Perth", "WA", "AU")
        }

        return 0
    }
}