package com.i19097842.curtin.edu.au.mad_assignment2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.MapFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.StructFrag
import com.i19097842.curtin.edu.au.mad_assignment2.models.GameMap
import com.i19097842.curtin.edu.au.mad_assignment2.models.Structure
import java.text.FieldPosition


/**
 * GameData.kt Activity: This is the games core activity.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameAct : MapFrag.MapListener, StructFrag.StructListener, AppCompatActivity() {

    private var mapFrag: MapFrag? = null
    private var selectedStruct: Structure? = null

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

        // attach the map fragment
        if (savedInstanceState == null) {
            mapFrag = MapFrag.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.gameFrameMap, it).commit()
            }
        }
    }

    /**
     * Handle map click
     * @see [MapFrag.MapListener.onMapClick]
     */
    override fun onMapClick(element: GameMap.MapElement, position: Int) {
        selectedStruct?.let {
            if (element.buildable && element.structure == null) {
                element.structure = selectedStruct
                mapFrag?.adapter?.notifyItemChanged(position)
            }
        }
    }

    /**
     * Handle structure selection
     * @see [StructFrag.StructListener.onStructureSelected]
     */
    override fun onStructureSelected(structure: Structure) {
        selectedStruct = structure
    }
}