package com.i19097842.curtin.edu.au.mad_assignment2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.MapFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.MetaFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.StructFrag
import com.i19097842.curtin.edu.au.mad_assignment2.models.Game
import com.i19097842.curtin.edu.au.mad_assignment2.models.GameMap
import com.i19097842.curtin.edu.au.mad_assignment2.models.Structure

/**
 * GameData.kt Activity: This is the games core activity.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameAct : MapFrag.MapListener, StructFrag.StructListener, MetaFrag.MetaListener,
    AppCompatActivity() {

    private var mapFrag: MapFrag? = null
    private var metaFrag: MetaFrag? = null
    private var selectedStruct: Structure? = null
    private var editMode: Game.EditMode = Game.EditMode.BUILD

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

        // attach the map and meta fragments
        if (savedInstanceState == null) {
            supportFragmentManager.let { fm ->
                fm.beginTransaction().run {
                    mapFrag = MapFrag.newInstance().also {
                        add(R.id.gameFrameMap, it)
                    }
                    metaFrag = MetaFrag.newInstance().also {
                        add(R.id.gameFrameMeta, it)
                    }
                    commit()
                }
            }
        }
    }

    /**
     * Handle map click
     * @see [MapFrag.MapListener.onMapClick]
     */
    override fun onMapClick(element: GameMap.MapElement, position: Int) {
        var updated = false

        updated = when (editMode) {
            Game.EditMode.BUILD -> selectedStruct != null && Game.get.putStructure(selectedStruct!!, position)
            Game.EditMode.DETAILS -> false
            Game.EditMode.DELETE -> Game.get.deleteStructure(position)
        }

        if (updated) mapFrag?.adapter?.notifyItemChanged(position)
    }

    /**
     * Handle structure selection
     * @see [StructFrag.StructListener.onStructureSelected]
     */
    override fun onStructureSelected(structure: Structure) {
        selectedStruct = structure
        metaFrag?.updateSelectedStructure(structure)
    }

    /**
     * Handle edit mode change by user
     * @see [MetaFrag.MetaListener.onEditModeChange]
     */
    override fun onEditModeChange(mode: Game.EditMode) {
        editMode = mode
    }
}