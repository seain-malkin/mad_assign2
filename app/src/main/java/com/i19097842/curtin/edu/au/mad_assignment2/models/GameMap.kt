package com.i19097842.curtin.edu.au.mad_assignment2.models

import com.i19097842.curtin.edu.au.mad_assignment2.lib.MapData

/**
 * Represents the map that the user can build structures upon
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 *
 * @property[height] The map height
 * @property[width] The map width
 */
class GameMap(
    val height: Int,
    val width: Int
) {
    /**
     * @property[grid] A two-dimensional array representing an (x,y) co-ordinate
     */
    private val grid = MapData.generateGrid(height, width)

    /**
     * @property[area] The total number of grid elements in the map
     */
    val area = height * width

    /**
     * Returns the [MapElement] at the given co-ordinate
     * @param[x] The x co-ordinate
     * @param[y] The y co-ordinate
     */
    fun get(x: Int, y: Int): MapElement {
        return grid[x][y]
    }

    /**
     * Given a one-dimensional index, returns the [MapElement] at the relative (x, y) co-ordinate
     * @param[position] The one-dimensional index of the two-dimensional grid
     */
    fun get(position: Int): MapElement {
        return get(position % HEIGHT, position / HEIGHT)
    }

    /**
     * Represents each grid element in the map. Each grid is split into a 2x2 grid and consists
     * of four drawables that are displayed in each sub-grid. The structure overlaps the entire
     * sub-grid.
     *
     * @property[buildable] Whether a structure can be placed on the element
     * @property[nw] North west
     * @property[ne] North east
     * @property[se] South east
     * @property[sw] South west
     * @property[structure] The [Structure] to place on top of the grid
     */
    class MapElement(
        val buildable: Boolean,
        val nw: Int,
        val ne: Int,
        val se: Int,
        val sw: Int,
        var structure: Structure? = null
    )
}