package com.i19097842.curtin.edu.au.mad_assignment2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.MapFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.MetaFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.StructListFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.ToolsFrag

/**
 * GameData.kt Activity: This is the games core activity. Content is displayed across four fragments
 * as listed:
 * 1. Tools: A toolbar with various actions
 * 2. Map: Displays the game map
 * 3. Structure List: A scrollable list of structures that can be built
 * 4. Meta: Ant overlay on the map that displays meta data and actions
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
            fm.beginTransaction().run {
                if (fm.findFragmentById(R.id.gameFrameTools) !is ToolsFrag) {
                    add(R.id.gameFrameTools, ToolsFrag.newInstance())
                }
                if (fm.findFragmentById(R.id.gameFrameMap) !is MapFrag) {
                    add(R.id.gameFrameMap, MapFrag.newInstance())
                }
                if (fm.findFragmentById(R.id.gameFrameStructList) !is StructListFrag) {
                    add(R.id.gameFrameStructList, StructListFrag.newInstance())
                }
                if (fm.findFragmentById(R.id.gameFrameMeta) !is MetaFrag) {
                    add(R.id.gameFrameMeta, MetaFrag.newInstance())
                }
                // Only commit transaction if something was added
                if (!isEmpty) commit()
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