package com.i19097842.curtin.edu.au.mad_assignment2

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

private const val ARG_MONEY = "settings.money"
private const val ARG_MAP_WIDTH = "settings.map_width"
private const val ARG_MAP_HEIGHT = "settings.map_height"

class SettingsAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val moneyEt = findViewById<EditText>(R.id.settingsMoney)
        val mapWidthEt = findViewById<EditText>(R.id.settingsMapWidth)
        val mapHeightEt = findViewById<EditText>(R.id.settingsMapHeight)
        val cancelBtn = findViewById<Button>(R.id.settingsActionCancel)
        val saveBtn = findViewById<Button>(R.id.settingsActionSave)

        // User Click Event: Save settings
        saveBtn.setOnClickListener {
            // Save settings data in an intent
            val settings = Intent()
            settings.putExtra(ARG_MONEY, moneyEt.text.toString().toInt())
            settings.putExtra(ARG_MAP_WIDTH, mapWidthEt.text.toString().toInt())
            settings.putExtra(ARG_MAP_HEIGHT, mapHeightEt.text.toString().toInt())

            // Return to caller activity with intent
            setResult(Activity.RESULT_OK, settings)
            finish()
        }

        // On cancel, just return to caller activity
        cancelBtn.setOnClickListener { finish() }
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

        @JvmStatic
        fun getInitialMoney(intent: Intent, default: Int) : Int {
            return intent.getIntExtra(ARG_MONEY, default)
        }

        @JvmStatic
        fun getMapWidth(intent: Intent, default: Int) : Int {
            return intent.getIntExtra(ARG_MAP_WIDTH, default)
        }

        @JvmStatic
        fun getMapHeight(intent: Intent, default: Int) : Int {
            return intent.getIntExtra(ARG_MAP_HEIGHT, default)
        }
    }
}