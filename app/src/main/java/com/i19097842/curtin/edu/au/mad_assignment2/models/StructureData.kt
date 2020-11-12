package com.i19097842.curtin.edu.au.mad_assignment2.models

import com.i19097842.curtin.edu.au.mad_assignment2.R


/**
 * Singleton class that creates and stores structure objects
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class StructureData private constructor() {
    val residential: Array<Structure> = arrayOf(
        Residential(R.drawable.ic_building1),
        Residential(R.drawable.ic_building2),
        Residential(R.drawable.ic_building3),
        Residential(R.drawable.ic_building4)
    )

    val commercial: Array<Structure> = arrayOf(
        Commercial(R.drawable.ic_building5),
        Commercial(R.drawable.ic_building6),
        Commercial(R.drawable.ic_building7),
        Commercial(R.drawable.ic_building8)
    )

    val roads: Array<Structure> = arrayOf(
        Road(R.drawable.ic_road_ew),
        Road(R.drawable.ic_road_ns),
        Road(R.drawable.ic_road_nw),
        Road(R.drawable.ic_road_ne),
        Road(R.drawable.ic_road_sw),
        Road(R.drawable.ic_road_se),
        Road(R.drawable.ic_road_new),
        Road(R.drawable.ic_road_sew),
        Road(R.drawable.ic_road_nse),
        Road(R.drawable.ic_road_nsw),
        Road(R.drawable.ic_road_nsew),
        Road(R.drawable.ic_road_n),
        Road(R.drawable.ic_road_e),
        Road(R.drawable.ic_road_s),
        Road(R.drawable.ic_road_w)
    )

    val trees: Array<Structure> = arrayOf(
        Tree(R.drawable.ic_tree1),
        Tree(R.drawable.ic_tree2),
        Tree(R.drawable.ic_tree3),
        Tree(R.drawable.ic_tree4)
    )

    companion object {
        /**
         * Singleton object reference
         */
        val get = StructureData()
    }
}