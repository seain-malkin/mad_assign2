package com.i19097842.curtin.edu.au.mad_assignment2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SettingsAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    companion object {
        /**
         * Static method for creating an intent for [SettingsAct] activity
         * @param[context] The caller context
         * @return An intent object
         */
        @JvmStatic
        fun getIntent(context: Context) : Intent {
            return Intent(context, SettingsAct::class.java)
        }
    }
}