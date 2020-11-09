package com.i19097842.curtin.edu.au.mad_assignment2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.i19097842.curtin.edu.au.mad_assignment2.models.Game

private const val LAUNCH_SETTINGS_ACTIVITY = 1

/**
 * Title Activity: Displays the app name and author with the option to change settings or start
 * the game. Each option launches a new activity. The application back button will return to this
 * activity
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class TitleAct : AppCompatActivity() {
    lateinit var game: Game
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

        // Launch Game Activity
        findViewById<Button>(R.id.titleActionMap)
            .setOnClickListener {
                // Launch the game
                game.start()
                startActivity(GameAct.getIntent(this))
            }

        // Launch Settings Activity
        findViewById<Button>(R.id.titleActionSettings)
            .setOnClickListener {
                startActivityForResult(SettingsAct.getIntent(
                    this,
                    Game.get.settings.initialMoney,
                    Game.get.settings.mapWidth,
                    Game.get.settings.mapHeight
                ), LAUNCH_SETTINGS_ACTIVITY)
            }
    }

    /**
     * @see AppCompatActivity.onActivityResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Expecting settings activity result
        if (requestCode == LAUNCH_SETTINGS_ACTIVITY && resultCode == Activity.RESULT_OK) {
            data?.let {
                Game.get.settings.run {
                    initialMoney = SettingsAct.getInitialMoney(it, initialMoney)
                    mapWidth = SettingsAct.getMapWidth(it, mapWidth)
                    mapHeight = SettingsAct.getMapHeight(it, mapHeight)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (::game.isInitialized) {
            game.save()
        }
    }
}