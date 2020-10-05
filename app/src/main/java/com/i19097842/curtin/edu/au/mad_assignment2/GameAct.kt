package com.i19097842.curtin.edu.au.mad_assignment2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.MapFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.MetaFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.StructListFrag

/**
 * GameData.kt Activity: This is the games core activity. Content is displayed across four fragments
 * as listed:
 * 2. Map: Displays the game map (Embedded in XML)
 * 3. Structure List: A scrollable list of structures that can be built
 * 4. Meta: Ant overlay on the map that displays meta data and actions (Embedded in XML)
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameAct : MapFrag.MapListener, AppCompatActivity() {
    /**
     * Injects the fragments into the main view and reinstates any saved state data
     * @see [AppCompatActivity.onCreate]
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Load fragments using the fragment manager
        supportFragmentManager.let { fm ->
            // Begin a transaction and add fragments if needed
            if (fm.findFragmentById(R.id.gameFrameStructList) !is StructListFrag) {
                fm.beginTransaction()
                    .add(R.id.gameFrameStructList, StructListFrag.newInstance()).commit()
            }
        }
    }

    companion object {
        /**
         * Static method for creating an [Intent] object for [GameAct]
         * @param[context] The caller activity [Context]
         * @return An [Intent] object relative to this class
         */
        @JvmStatic
        fun getIntent(context: Context): Intent {
            return Intent(context, GameAct::class.java)
        }
    }
}