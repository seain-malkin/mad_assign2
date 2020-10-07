package com.i19097842.curtin.edu.au.mad_assignment2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.i19097842.curtin.edu.au.mad_assignment2.R
import com.i19097842.curtin.edu.au.mad_assignment2.models.Structure
import com.i19097842.curtin.edu.au.mad_assignment2.models.StructureData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StructSelectFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class StructSelectFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StructSelectFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String = "", param2: String = "") =
            StructSelectFrag().apply {
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
        val view = inflater.inflate(R.layout.fragment_struct_select, container, false)
        setupNavMenu(view.findViewById(R.id.structSelectMenu))
        setupList(view.findViewById(R.id.structSelectRV))

        return view
    }

    /**
     * Sets up event listeners on the menu items
     * @param[menu] Reference to the bottom menu
     */
    private fun setupNavMenu(menu: BottomNavigationView) {
        menu.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_res -> Log.i("BottomNav", "Residential")
                R.id.item_com -> Log.i("BottomNav", "Commercial")
                else -> false
            }

            true
        }
        menu.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.item_res -> Log.i("BottomNav", "Residential Reselect")
                R.id.item_com -> Log.i("BottomNav", "Commercial Reselct")
                else -> false
            }

            true
        }
    }

    /**
     * Sets up the [RecyclerView] list that displays the selectable structures
     * @param[rv] Reference to the recycler view
     */
    private fun setupList(rv: RecyclerView) {
        rv.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        rv.adapter = StructAdapter(StructureData.get.residential)
    }

    /**
     * The [RecyclerView.Adapter] subclass that handles the views and data binding for the
     * structure list
     * @property[items] An array of items to display
     */
    private inner class StructAdapter(
        var items: Array<Structure>
    ): RecyclerView.Adapter<StructListVH>() {
        /** @see [RecyclerView.Adapter.onCreateViewHolder] */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StructListVH {
            return StructListVH(LayoutInflater.from(activity), parent)
        }

        /** @see [RecyclerView.Adapter.onBindViewHolder] */
        override fun onBindViewHolder(holder: StructListVH, position: Int) {
            holder.bind(items[position])
        }

        /** @see [RecyclerView.Adapter.getItemCount] */
        override fun getItemCount(): Int {
            return items.size
        }
    }

    /**
     * The [RecyclerView.ViewHolder] subclass used by the adapter to create each view
     * @param[li] The inflater to use for the view
     * @param[parent] The view group to attach the view to
     */
    inner class StructListVH(
        li: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder(
        li.inflate(R.layout.struct_list, parent, false)
    ) {
        /** Find view elements */
        private val image:ImageView = itemView.findViewById(R.id.structListImage)

        /**
         * Binds the data item to the view
         * @param[item] The structure to bind
         */
        fun bind(item: Structure) {
            image.setImageResource(item.drawable)
        }
    }
}