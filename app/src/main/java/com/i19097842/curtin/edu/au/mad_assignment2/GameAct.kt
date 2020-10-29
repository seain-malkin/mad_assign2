package com.i19097842.curtin.edu.au.mad_assignment2

import android.app.Activity
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

private const val LAUNCH_DETAILS_ACTIVITY = 1

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Expecting details activity result
        if (requestCode == LAUNCH_DETAILS_ACTIVITY && resultCode == Activity.RESULT_OK) {
            data?.let {
                // Update the structure name
                Game.get.map.get(DetailsAct.getCol(it), DetailsAct.getRow(it)).run {
                    DetailsAct.getName(it)?.let { name -> this.structure?.name = name }
                }
            }
        }
    }

    /**
     * Begins the Details Activity
     * @param[structure] The structure the details explain
     * @param[position] The position of the structure on the map
     */
    private fun launchDetailsActivity(structure: Structure, position: Int) {
        startActivityForResult(DetailsAct.getIntent(
            this,
            Game.get.map.getYFromPos(position),
            Game.get.map.getXFromPos(position),
            structure.getTypeString(),
            structure.name
        ), LAUNCH_DETAILS_ACTIVITY)
    }

    /**
     * Handle map click
     * @see [MapFrag.MapListener.onMapClick]
     */
    override fun onMapClick(element: GameMap.MapElement, position: Int) {
        var updated = false

        // Perform appropriate function depending on the edit mode selected
        // Determine if map needs to be updated afterwards
        updated = when (editMode) {
            Game.EditMode.BUILD -> selectedStruct != null && Game.get.putStructure(selectedStruct!!, position)
            Game.EditMode.DETAILS -> {
                // Only launch if a structure exists
                element.structure?.let { launchDetailsActivity(it, position) }
                // No visual map changes, no update needed
                false
            }
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