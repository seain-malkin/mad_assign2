package com.i19097842.curtin.edu.au.mad_assignment2

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

/**
 * Argument keys for parsing data
 */
private const val PACKAGE = "com.i19097842.curtin.edu.au.mad_assignment2"
private const val ARG_MONEY = "$PACKAGE.settings.money"
private const val ARG_NAME = "$PACKAGE.settings.name"
private const val ARG_MAP_WIDTH = "$PACKAGE.settings.map_width"
private const val ARG_MAP_HEIGHT = "$PACKAGE.settings.map_height"
private const val ARG_NEW_GAME = "$PACKAGE.settings.new_game"

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
        val nameEt = findViewById<EditText>(R.id.settingsName)
        val mapWidthEt = findViewById<EditText>(R.id.settingsMapWidth)
        val mapHeightEt = findViewById<EditText>(R.id.settingsMapHeight)
        val cancelBtn = findViewById<Button>(R.id.settingsActionCancel)
        val saveBtn = findViewById<Button>(R.id.settingsActionSave)

        // Display current settings values. The default values here don't matter
        nameEt.setText(intent.getStringExtra(ARG_NAME))
        moneyEt.setText(intent.getIntExtra(ARG_MONEY, 0).toString())
        mapWidthEt.setText(intent.getIntExtra(ARG_MAP_WIDTH, 0).toString())
        mapHeightEt.setText(intent.getIntExtra(ARG_MAP_HEIGHT, 0).toString())

        // Disable certain inputs if not allowed to change once game started
        if (!intent.getBooleanExtra(ARG_NEW_GAME, true)) {
            moneyEt.isEnabled = false
            mapWidthEt.isEnabled = false
            mapHeightEt.isEnabled = false
        }

        // User Click Event: Save settings
        saveBtn.setOnClickListener {
            // Save settings data in an intent
            val settings = Intent()
            settings.putExtra(ARG_NAME, nameEt.text.toString())
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
         * @param[name] The town name
         * @param[initialMoney] Amount of money to begin the game with
         * @param[mapWidth] The width of the game map
         * @param[mapHeight] The height of the game map
         * @param[newGame] If false, some settings can't be modified
         * @return An intent object
         */
        @JvmStatic
        fun getIntent(
            context: Context,
            name: String,
            initialMoney: Int,
            mapWidth: Int,
            mapHeight: Int,
            newGame: Boolean = true
        ) : Intent {
            // Create intent with initial arguments
            val intent = Intent(context, SettingsAct::class.java)
            intent.putExtra(ARG_NAME, name)
            intent.putExtra(ARG_MONEY, initialMoney)
            intent.putExtra(ARG_MAP_WIDTH, mapWidth)
            intent.putExtra(ARG_MAP_HEIGHT, mapHeight)
            intent.putExtra(ARG_NEW_GAME, newGame)

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
         * Returns name from intent
         * @param[intent] The activity intent
         * @param[default] The default value
         * @return The name setting
         */
        @JvmStatic
        fun getName(intent: Intent, default: String): String {
            return intent.getStringExtra(ARG_NAME) ?: default
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