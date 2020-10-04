package com.i19097842.curtin.edu.au.mad_assignment2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.i19097842.curtin.edu.au.mad_assignment2.R
import com.i19097842.curtin.edu.au.mad_assignment2.models.GameData
import com.i19097842.curtin.edu.au.mad_assignment2.models.GameMap
import com.i19097842.curtin.edu.au.mad_assignment2.models.Structure

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Displays the game map and handles user interactions and communication with the main activity
 * Use the [MapFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val mapAdapter = MapAdapter(GameData.get.map)

    /**
     * @see Fragment.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * @see Fragment.onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Set up the RecyclerView by attaching a layout manager and adapter
        view.findViewById<RecyclerView>(R.id.mapRV).let {
            it.layoutManager = GridLayoutManager(
                activity,
                GameData.get.map.height,
                GridLayoutManager.HORIZONTAL,
                false
            )
            it.adapter = mapAdapter
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param[param1] Parameter 1.
         * @param[param2] Parameter 2.
         * @return A new instance of fragment [MapFrag].
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String = "", param2: String = "") =
            MapFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /**
     * The [RecyclerView.Adapter] subclass to handle displaying the viewable grid views.
     * @property[map] The map being displayed
     */
    inner class MapAdapter(
        private val map: GameMap
    ) : RecyclerView.Adapter<GridVH>() {
        /**
         * @see RecyclerView.Adapter.onCreateViewHolder
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridVH {
            return GridVH(LayoutInflater.from(activity), parent)
        }

        /**
         * @see RecyclerView.Adapter.onBindViewHolder
         */
        override fun onBindViewHolder(holder: GridVH, position: Int) {
            holder.bind(map.get(position))
        }

        /**
         * @see RecyclerView.Adapter
         */
        override fun getItemCount(): Int {
            return map.area
        }
    }

    /**
     * The [RecyclerView.ViewHolder] subclass that displays each map grid and user events on each
     * grid.
     */
    inner class GridVH(
        li: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(li.inflate(R.layout.map_grid, parent, false)) {
        /**
         * Find object references for view elements
         */
        private val nw = itemView.findViewById<ImageView>(R.id.gridNW)
        private val ne = itemView.findViewById<ImageView>(R.id.gridNE)
        private val se = itemView.findViewById<ImageView>(R.id.gridSE)
        private val sw = itemView.findViewById<ImageView>(R.id.gridSW)
        private val struct = itemView.findViewById<ImageView>(R.id.gridStruct)

        init {
            // Dynamically change the views dimensions based on screen size
            (parent.measuredHeight / GameData.get.map.height + 1).let { dim ->
                itemView.layoutParams.let {
                    it.height = dim
                    it.width = dim
                }
            }

            // Set click event on entire view element
            itemView.setOnClickListener { Log.i("Grid CLick", "Yes") }
        }

        /**
         * Binds the [GameMap.MapElement] data to the view elements
         * @param[element] The element being displayed in this view
         */
        fun bind(element: GameMap.MapElement) {
            // Set the drawable for each grid
            nw.setImageResource(element.nw)
            ne.setImageResource(element.ne)
            se.setImageResource(element.se)
            sw.setImageResource(element.sw)

            // Structure could be null. Either set the drawable or remove any drawable already
            // set to the element
            if (element.structure is Structure) {
                struct.setImageResource(element.structure!!.drawable)
            }
            else {
                struct.setImageDrawable(null)
            }
        }
    }
}