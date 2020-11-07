package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.content.ContentValues
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
                null, null, null
            ).run {
                if (count > 0) {
                    // Initialise the array and use the appropriate cursor based on coordinates
                    grid = Array(height) { y ->
                        Array<MapElement>(width) { x ->
                            // If move fails then something went terribly wrong :(
                            if (!move(y * x -1)) {
                                throw IllegalStateException("Error loading all map elements")
                            }

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
                            if (!isNull(6)) {
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
     */
    fun save(gameId: Int) {
        // Perform save for every grid position
        for (i in 1..area) {
            val element = get(i - 1)
            val cv = ContentValues()

            GameSchema.map.cols.let {
                cv.put(it.gameId, gameId)
                cv.put(it.gridIndex, i)
                cv.put(it.buildable, if (element.buildable) 1 else 0) // convert boolean to int
                cv.put(it.nw, element.nw)
                cv.put(it.ne, element.ne)
                cv.put(it.sw, element.sw)
                cv.put(it.se, element.se)
                element.structure?.let { structure ->
                    cv.put(it.structureType, structure.getTypeString())
                    cv.put(it.drawable, structure.drawable)
                }
            }

            element.id = dbHelper.save(GameSchema.map, cv, element.id)
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