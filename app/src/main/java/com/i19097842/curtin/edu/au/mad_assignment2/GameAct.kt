package com.i19097842.curtin.edu.au.mad_assignment2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.MapFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.StructSelectFrag
import com.i19097842.curtin.edu.au.mad_assignment2.models.GameMap



/**
 * GameData.kt Activity: This is the games core activity.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameAct : MapFrag.MapListener, AppCompatActivity() {
    /** @property[structSelectFrag] Instance of the structure select fragment */
    private var structSelectFrag: StructSelectFrag? = null

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

    /**
     * Injects the fragments into the main view and reinstates any saved state data
     * @see [AppCompatActivity.onCreate]
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }

    override fun onMapClick(element: GameMap.MapElement?) {
        Log.i("GameAct", "Map Click")
    }
}