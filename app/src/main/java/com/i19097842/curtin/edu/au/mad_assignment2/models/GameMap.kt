package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.content.ContentValues
import android.util.Log
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameDbHelper
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameSchema
import com.i19097842.curtin.edu.au.mad_assignment2.lib.MapData
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

/**
 * Represents the map that the user can build structures upon
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 *
 * @property[width] The map width
 * @property[height] The map height
 * @property[gameId] The database id for the game
 */
class GameMap(
    val dbHelper: GameDbHelper,
    val width: Int,
    val height: Int,
    val gameId: Int? = null
) {
    val area = height * width
    val grid: Array<out Array<MapElement>>

    init {
        // Attempt to retrieve map grid from database
        val table = GameSchema.map

        if (gameId != null) {
            dbHelper.db().query(
                table.name,
                arrayOf(
                    table.cols.id,
                    table.cols.gridIndex,
                    table.cols.buildable,
                    table.cols.nw,
                    table.cols.ne,
                    table.cols.sw,
                    table.cols.se,
                    table.cols.structureType,
                    table.cols.drawable
                ),
                "${table.cols.gameId} = ?",
                arrayOf(gameId.toString()),
                null, null,
                "grid_index ASC"
            ).run {
                if (count > 0) {
                    Log.i("GameMap", "Grid count = $count")
                    // Initialise the array and use the appropriate cursor based on coordinates
                    grid = Array(height) { y ->
                        Log.i("GameMap", "y = $y")
                        Array<MapElement>(width) { x ->
                            Log.i("GampeMap", "x = $x")
                            moveToNext()
                            Log.i("Grid", "${getInt(getColumnIndex(table.cols.gridIndex))}")
                            val element = MapElement(
                                getInt(getColumnIndex(table.cols.buildable)) == 1,
                                getInt(getColumnIndex(table.cols.nw)),
                                getInt(getColumnIndex(table.cols.ne)),
                                getInt(getColumnIndex(table.cols.sw)),
                                getInt(getColumnIndex(table.cols.se)),
                                null,
                                getInt(getColumnIndex(table.cols.id))
                            )

                            // If structure stored in database create a structure object
                            if (!isNull(getColumnIndex(table.cols.structureType))) {
                                element.structure = Structure.factory(
                                    getString(getColumnIndex(table.cols.structureType)),
                                    getInt(getColumnIndex(table.cols.drawable))
                                )
                            }

                            element
                        }
                    }
                } else {
                    // It's possible a game is saved but the map wasn't initialised
                    grid = MapData.generateGrid(height, width)
                }

                close()
            }
        } else {
            grid = MapData.generateGrid(height, width)
        }
    }

    /**
     * Returns the [MapElement] at the given co-ordinate
     * @param[x] The x co-ordinate
     * @param[y] The y co-ordinate
     */
    fun get(x: Int, y: Int): MapElement {
        return grid[y][x]
    }

    /**
     * Given a one-dimensional index, returns the [MapElement] at the relative (x, y) co-ordinate
     * @param[position] The one-dimensional index of the two-dimensional grid
     */
    fun get(position: Int): MapElement {
        return get(getXFromPos(position), getYFromPos(position))
    }

    /**
     * Returns the x coordinate from the global position
     * @param[int] The position on the grid
     * @return The x coordinate
     */
    fun getXFromPos(position: Int) : Int {
        return position / height
    }

    /**
     * Returns the y coordinate from the global position
     * @param[int] The position on the grid
     * @return The y coordinate
     */
    fun getYFromPos(position: Int ) : Int {
        return position % height
    }

    /**
     * Saves each grid element to persistent storage
     * @param[gameId] The database id of the game being saved
     */
    fun save(gameId: Int) {
        var gridIndex = 0
        for (y in 0..(height -1)) {
            for (x in 0..(width -1)) {
                val cv = ContentValues()
                GameSchema.map.cols.let {
                    cv.put(it.gameId, gameId)
                    cv.put(it.gridIndex, gridIndex++)
                    cv.put(it.buildable, if (grid[y][x].buildable) 1 else 0) // convert boolean to int
                    cv.put(it.nw, grid[y][x].nw)
                    cv.put(it.ne, grid[y][x].ne)
                    cv.put(it.sw, grid[y][x].sw)
                    cv.put(it.se, grid[y][x].se)
                    grid[y][x].structure?.let { structure ->
                        cv.put(it.structureType, structure.getTypeString())
                        cv.put(it.drawable, structure.drawable)
                    }
                }
                grid[y][x].id = dbHelper.save(GameSchema.map, cv, grid[y][x].id)
            }
        }
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
     * @property[id] The database id of this element
     */
    class MapElement(
        val buildable: Boolean,
        val nw: Int,
        val ne: Int,
        val sw: Int,
        val se: Int,
        var structure: Structure? = null,
        var id: Int? = null
    )
}