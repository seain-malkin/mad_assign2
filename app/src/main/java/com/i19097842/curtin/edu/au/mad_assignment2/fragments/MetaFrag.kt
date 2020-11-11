package com.i19097842.curtin.edu.au.mad_assignment2.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.i19097842.curtin.edu.au.mad_assignment2.R
import com.i19097842.curtin.edu.au.mad_assignment2.models.Game
import com.i19097842.curtin.edu.au.mad_assignment2.models.Structure
import java.lang.RuntimeException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MetaFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class MetaFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    /** Current edit mode with default  */
    private var editMode: Game.EditMode = Game.EditMode.BUILD

    /**
     * Interface to enable fragment->activity communication
     */
    interface MetaListener {
        /**
         * Triggered when user changes the edit mode
         * @param[mode] The new mode to apply
         */
        fun onEditModeChange(mode: Game.EditMode)

        /**
         * Triggered when the user presses the next tick button
         */
        fun onTickRequest()
    }

    /** Reference to context caller */
    private var listener: MetaListener? = null

    /** References to view elements */
    private var selStructImg: ImageView? = null
    private var selStructCard: CardView? = null
    private var delStructImg: ImageView? = null
    private var delStructCard: CardView? = null
    private var detStructImg: ImageView? = null
    private var detStructCard: CardView? = null
    private var modeLayout: LinearLayout? = null

    /**
     * The dimensions of the mode change buttons. Calculated at runtime.
     */
    private var modeImgLarge: Int = 0
    private var modeImgSmall: Int = 0

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MetaFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String = "", param2: String = "") =
            MetaFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /** @see [Fragment.onCreate] */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /** @see [Fragment.onCreateView] */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_meta, container, false)

        // Find view elements
        val tickValue = view.findViewById<TextView>(R.id.nextTickValue)
        selStructImg = view.findViewById(R.id.selStructureImg)
        selStructCard = view.findViewById(R.id.selStructureCard)
        delStructImg = view.findViewById(R.id.delStructureImg)
        delStructCard = view.findViewById(R.id.delStructureCard)
        detStructImg = view.findViewById(R.id.detStructureImg)
        detStructCard = view.findViewById(R.id.detStructureCard)

        // Get the mode image sizes
        selStructImg?.layoutParams?.let {
            modeImgLarge = it.height
        }
        delStructImg?.layoutParams?.let {
            modeImgSmall = it.height
        }

        // Set UI element values
        tickValue.setText(getString(R.string.meta_tick_value, Game.get.values.ticks))

        // Attach user events to card elements
        selStructCard?.setOnClickListener { updateEditMode(Game.EditMode.BUILD) }
        delStructCard?.setOnClickListener { updateEditMode(Game.EditMode.DELETE) }
        detStructCard?.setOnClickListener { updateEditMode(Game.EditMode.DETAILS) }

        // Attach click event to next tick button
        view.findViewById<CardView>(R.id.nextTickCV).setOnClickListener {
            listener?.onTickRequest()
            tickValue.setText(getString(R.string.meta_tick_value, Game.get.values.ticks))
        }

        return view
    }

    /**
     * Updates the view elements to represent the new mode and notifies any listeners
     * @param[mode] The new mode to apply
     */
    fun updateEditMode(mode: Game.EditMode) {
        editMode = mode

        // Reference the card img to focus
        val img = when(mode) {
            Game.EditMode.BUILD -> selStructImg
            Game.EditMode.DELETE -> delStructImg
            Game.EditMode.DETAILS -> detStructImg
        }

        // Enlarge the referenced card image
        changeElementSize(img!!, modeImgLarge)

        // Reduce size of other card images
        if (img != selStructImg) {
            changeElementSize(selStructImg!!, modeImgSmall)
        }
        if (img != delStructImg) {
            changeElementSize(delStructImg!!, modeImgSmall)
        }
        if (img != detStructImg) {
            changeElementSize(detStructImg!!, modeImgSmall)
        }

        listener?.onEditModeChange(mode)
    }

    private fun changeElementSize(element: View, size: Int) {
        element.layoutParams.height = size
        element.layoutParams.width = size
        element.requestLayout()
    }


    /**
     * Changes the drawable associated with the selected structure
     * @param[structure] The structure that is selected for placement on map
     */
    fun updateSelectedStructure(structure: Structure) {
        selStructImg?.setImageResource(structure.drawable)

        selStructCard?.let {
            if (it.visibility == View.GONE) {
                it.visibility = View.VISIBLE
            }
        }

        // Change to build mode if not already
        if (editMode != Game.EditMode.BUILD) {
            updateEditMode(Game.EditMode.BUILD)
        }
    }

    /**
     * Ensure caller implements interface
     * @see [Fragment.onAttach]
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MetaListener) {
            listener = context
        }
        else {
            throw RuntimeException(context.toString() +
                    " must implement MetaListener")
        }
    }

    /**
     * Remove context listener reference to avoid memory leaks
     * @see [Fragment.onDetach]
     */
    override fun onDetach() {
        listener = null
        super.onDetach()
    }
}