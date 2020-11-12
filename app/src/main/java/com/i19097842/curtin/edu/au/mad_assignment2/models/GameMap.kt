package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.content.ContentValues
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameDbHelper
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameSchema
import com.i19097842.curtin.edu.au.mad_assignment2.lib.MapData

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
    val gameId: Int
) {
    val area = height * width
    val grid: Array<out Array<MapElement>>

    init {
        // Attempt to retrieve map grid from database
        val table = GameSchema.map
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
                table.cols.drawable,
                table.cols.name
            ),
            "${table.cols.gameId} = ?",
            arrayOf(gameId.toString()),
            null, null,
            "grid_index ASC"
        ).run {
            if (count > 0) {
                // Initialise the array and use the appropriate cursor based on coordinates
                grid = Array(height) { y ->
                    Array<MapElement>(width) { x ->
                        moveToNext()
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
                                getInt(getColumnIndex(table.cols.drawable)),
                                getString(getColumnIndex(table.cols.name))
                            )
                        }
                        element
                    }
                }
            } else {
                grid = MapData.generateGrid(height, width)
                save(gameId)
            }
            close()
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
                saveMapElement(gameId, grid[y][x], gridIndex++)
            }
        }
    }

    /**
     * Creates a [ContentValues] object to insert into the database
     * @param[element] The map element being saved
     * @param[gameId] The current game id
     * @param[gridIndex] The elements grid index. Only required on frist save
     * @return The database ready [ContentValues] object
     */
    private fun getMapElementCV(
        element: MapElement, gameId: Int, gridIndex: Int? = null
    ): ContentValues {
        val cv = ContentValues()
        GameSchema.map.cols.let {
            cv.put(it.gameId, gameId)
            gridIndex?.let { gi -> cv.put(it.gridIndex, gi) }
            cv.put(it.buildable, if (element.buildable) 1 else 0) // convert boolean to int
            cv.put(it.nw, element.nw)
            cv.put(it.ne, element.ne)
            cv.put(it.sw, element.sw)
            cv.put(it.se, element.se)
            // Need to save null values to overwrite structures that have been deleted
            var structure: String? = null
            var drawable: Int? = null
            var name: String? = null
            element.structure?.let { struct ->
                structure = struct.getTypeString()
                drawable = struct.drawable
                name = struct.name
            }
            cv.put(it.structureType, structure)
            cv.put(it.drawable, drawable)
            cv.put(it.name, name)
        }

        return cv
    }

    /**
     * Saves an individual map element
     * @param[gameId] The current game id
     * @param[element] The map element to save
     * @param[gridIndex] The elements global grid index. Only needed on first save
     */
    fun saveMapElement(gameId: Int, element: MapElement, gridIndex: Int? = null) {
        element.id = dbHelper.save(
            GameSchema.map,
            getMapElementCV(element, gameId, gridIndex),
            element.id
        )
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