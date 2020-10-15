package com.i19097842.curtin.edu.au.mad_assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

/**
 * Title Activity: Displays the app name and author with the option to change settings or start
 * the game. Each option launches a new activity. The application back button will return to this
 * activity
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class TitleAct : AppCompatActivity() {
    /**
     * Initialises activity elements and state.
     * @see AppCompatActivity.onCreate]
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)

        // Update author information
        findViewById<TextView>(R.id.titleAuthorName).text =
            getString(R.string.title_author,
            getString(R.string.author_name),
            getString(R.string.author_id))

        // Set action button events
        findViewById<Button>(R.id.titleActionMap)
            .setOnClickListener { startActivity(GameAct.getIntent(this)) }
        findViewById<Button>(R.id.titleActionSettings)
            .setOnClickListener { startActivity(SettingsAct.getIntent(this)) }
    }
}