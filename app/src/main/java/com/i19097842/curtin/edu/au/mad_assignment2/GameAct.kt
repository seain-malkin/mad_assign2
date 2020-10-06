package com.i19097842.curtin.edu.au.mad_assignment2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.MapFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.StructListFrag
import com.i19097842.curtin.edu.au.mad_assignment2.fragments.StructSelectFrag

/** Arguments for state bundles */
private const val ARG_STRUCT_MODE
        = "com.i19097842.curtin.edu.au.mad_assignment2.game_act.arg_struct_mode"

/** Constants for determining which fragment is loaded as the structure fragment */
private const val STRUCT_FRAG_SELECT = "struct_frag_select"
private const val STRUCT_FRAG_LIST = "struct_frag_list"


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
    /** @property[structFragMode] The current mode of the structure fragment */
    private var structFragMode = STRUCT_FRAG_SELECT

    /** @property[structListFrag] Instance of the structure list fragment */
    private var structListFrag: StructListFrag? = null

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

        savedInstanceState?.let {
            structFragMode = savedInstanceState.getString(ARG_STRUCT_MODE, STRUCT_FRAG_SELECT)
        }

        // Decide which structure fragment to display
        when (structFragMode == STRUCT_FRAG_SELECT) {
            true -> loadStructFragSelect()
            false -> loadStructFragList()
        }
    }

    /**
     * Save activity state
     * @see [AppCompatActivity.onSaveInstanceState]
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ARG_STRUCT_MODE, structFragMode)
        super.onSaveInstanceState(outState)
    }

    /**
     * Loads the structure select fragment
     */
    private fun loadStructFragSelect() {
        // Create an instance if needed
        if (structSelectFrag == null) {
            supportFragmentManager.let { fm ->
                fm.findFragmentByTag(STRUCT_FRAG_SELECT).let {
                    // Either reference the found fragment or create a new instance
                    structSelectFrag = when (it) {
                        is StructSelectFrag -> it
                        else -> StructSelectFrag.newInstance()
                    }
                }
            }
        }
        // Display fragment and hide its counterpart
        toggleStructFrag(structSelectFrag, structListFrag)
    }

    /**
     * Loads the structure list fragment
     * Exactly the same logic as previous function but a generic version won't work as you
     * can't call a static method from a generic class (as far as I could tell)
     */
    private fun loadStructFragList() {
        // Create an instance if needed
        if (structListFrag == null) {
            supportFragmentManager.let { fm ->
                fm.findFragmentByTag(STRUCT_FRAG_LIST).let {
                    // Either reference the found fragment or create a new instance
                    structListFrag = when (it) {
                        is StructListFrag -> it
                        else -> StructListFrag.newInstance()
                    }
                }
            }
        }
        // Display fragment and hide its counterpart
        toggleStructFrag(structListFrag, structSelectFrag)
    }

    /**
     * Displays one fragment and hides the other
     * @param[on] The fragment to display
     * @param[off] The fragment to hide
     */
    private fun toggleStructFrag(on: Fragment?, off: Fragment?) {
        supportFragmentManager.let { fm ->
            fm.beginTransaction().run {
                on?.let { frag ->
                    when (frag.isAdded) {
                        true -> show(frag)
                        false -> add(R.id.gameFrameStruct, frag,
                            if (structFragMode == STRUCT_FRAG_SELECT)
                                STRUCT_FRAG_SELECT else STRUCT_FRAG_LIST)
                    }
                    off?.let { hide(it) }
                    commit()
                }
            }
        }
    }
}