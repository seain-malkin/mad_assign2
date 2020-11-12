package com.i19097842.curtin.edu.au.mad_assignment2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.i19097842.curtin.edu.au.mad_assignment2.models.Game
import com.i19097842.curtin.edu.au.mad_assignment2.models.Settings

private const val LAUNCH_SETTINGS_ACTIVITY_RESUME = 1
private const val LAUNCH_SETTINGS_ACTIVITY_NEW = 2

/**
 * Title Activity: Displays the app name and author with the option to change settings or start
 * the game. Each option launches a new activity. The application back button will return to this
 * activity
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class TitleAct : AppCompatActivity() {
    private lateinit var game: Game
    private lateinit var settingsBtn: Button
    private lateinit var resumeBtn: Button
    /**
     * Initialises activity elements and state.
     * @see AppCompatActivity.onCreate]
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)

        if (!::game.isInitialized) {
            game = Game.get
        }

        game.load(applicationContext)

        // Update author information
        findViewById<TextView>(R.id.titleAuthorName).text =
            getString(R.string.title_author,
            getString(R.string.author_name),
            getString(R.string.author_id))

        // Set up button events and decide which buttons are visible

        // Resume game button
        resumeBtn = findViewById(R.id.titleActionResume)
        // On click: Launch game
        resumeBtn.setOnClickListener { startActivity(GameAct.getIntent(this)) }

        if (game.inProgress()) {
            // Show button as game in progress
            resumeBtn.visibility = View.VISIBLE
        } else {
            // Hide button as no game created yet
            resumeBtn.visibility = View.GONE
        }

        // New game button
        // This button is always visible. It will launch the settings activity to request
        // settings for the new game.
        findViewById<Button>(R.id.titleActionNew)
            .setOnClickListener {
                // Create a new settings object to access the default values
                val settings = Settings(game.dbHelper)

                startActivityForResult(SettingsAct.getIntent(
                    this,
                    "Newtownsville",
                    settings.initialMoney,
                    settings.mapWidth,
                    settings.mapHeight
                ), LAUNCH_SETTINGS_ACTIVITY_NEW)
            }

        // Settings button
        // Only visible if a game is in progress. Otherwise the new game button will launch settings
        settingsBtn = findViewById(R.id.titleActionSettings)

        if (game.inProgress()) {
            settingsBtn.visibility = View.VISIBLE
        }

        settingsBtn.setOnClickListener {
            startActivityForResult(
                SettingsAct.getIntent(
                    this,
                    game.title,
                    game.settings.initialMoney,
                    game.settings.mapWidth,
                    game.settings.mapHeight,
                    false
                ), LAUNCH_SETTINGS_ACTIVITY_RESUME
            )
        }
    }

    /**
     * Displays the resume and settings buttons
     */
    private fun updateUI() {
        if (resumeBtn.visibility != View.VISIBLE) {
            resumeBtn.visibility = View.VISIBLE
        }
        if (settingsBtn.visibility != View.VISIBLE) {
            settingsBtn.visibility = View.VISIBLE
        }
    }

    /**
     * @see AppCompatActivity.onActivityResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Expecting settings activity result
        if (requestCode == LAUNCH_SETTINGS_ACTIVITY_RESUME && resultCode == Activity.RESULT_OK) {
            data?.let {
                // Only the title could have been changed as the game has begun already
                game.title = SettingsAct.getName(it, game.title)
                game.save()
            }
        }
        else if (requestCode == LAUNCH_SETTINGS_ACTIVITY_NEW && resultCode == Activity.RESULT_OK) {
            data?.let {
                // Create a new settings object from the activity result
                val settings = Settings(game.dbHelper)
                settings.run {
                    initialMoney = SettingsAct.getInitialMoney(it, initialMoney)
                    mapWidth = SettingsAct.getMapWidth(it, mapWidth)
                    mapHeight = SettingsAct.getMapHeight(it, mapHeight)
                }
                // Update UI so the appropriate buttons are displayed
                updateUI()

                // Set the title of the new game
                game.title = SettingsAct.getName(it, game.title)

                // Start a new game and launch the game activity
                game.newGame(SettingsAct.getName(it, game.title), settings)
                startActivity(GameAct.getIntent(this))
            }
        }
    }
}