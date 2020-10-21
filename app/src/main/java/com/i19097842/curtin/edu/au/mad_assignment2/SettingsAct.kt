package com.i19097842.curtin.edu.au.mad_assignment2

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText

/**
 * Argument keys for parsing data
 */
private const val ARG_MONEY = "settings.money"
private const val ARG_MAP_WIDTH = "settings.map_width"
private const val ARG_MAP_HEIGHT = "settings.map_height"

/**
 * Settings Activity: Displays editable settings that can be changed prior to the game starting.
 * Once the game has begun, only certain settings can be changed.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class SettingsAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Find view elements
        val moneyEt = findViewById<EditText>(R.id.settingsMoney)
        val mapWidthEt = findViewById<EditText>(R.id.settingsMapWidth)
        val mapHeightEt = findViewById<EditText>(R.id.settingsMapHeight)
        val cancelBtn = findViewById<Button>(R.id.settingsActionCancel)
        val saveBtn = findViewById<Button>(R.id.settingsActionSave)

        // Display current settings values. The default values here don't matter
        moneyEt.setText(intent.getIntExtra(ARG_MONEY, 0).toString())
        mapWidthEt.setText(intent.getIntExtra(ARG_MAP_WIDTH, 0).toString())
        mapHeightEt.setText(intent.getIntExtra(ARG_MAP_HEIGHT, 0).toString())


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
        fun getIntent(context: Context, initialMoney: Int, mapWidth: Int, mapHeight: Int) : Intent {
            // Create intent with initial arguments
            val intent = Intent(context, SettingsAct::class.java)
            intent.putExtra(ARG_MONEY, initialMoney)
            intent.putExtra(ARG_MAP_WIDTH, mapWidth)
            intent.putExtra(ARG_MAP_HEIGHT, mapHeight)

            return intent
        }

        /**
         * Returns initialMoney from intent
         * @param[intent] The activity intent
         * @param[default] The default value
         * @return The initialMoney setting
         */
        @JvmStatic
        fun getInitialMoney(intent: Intent, default: Int) : Int {
            return intent.getIntExtra(ARG_MONEY, default)
        }

        /**
         * Returns mapWidth from intent
         * @param[intent] The activity intent
         * @param[default] The default value
         * @return The mapWidth setting
         */
        @JvmStatic
        fun getMapWidth(intent: Intent, default: Int) : Int {
            return intent.getIntExtra(ARG_MAP_WIDTH, default)
        }

        /**
         * Returns mapHeight from intent
         * @param[intent] The activity intent
         * @param[default] The default value
         * @return The mapHeight setting
         */
        @JvmStatic
        fun getMapHeight(intent: Intent, default: Int) : Int {
            return intent.getIntExtra(ARG_MAP_HEIGHT, default)
        }
    }
}