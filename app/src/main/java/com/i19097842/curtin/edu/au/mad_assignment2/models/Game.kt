package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.util.Log
import com.i19097842.curtin.edu.au.mad_assignment2.lib.MapData
import java.lang.RuntimeException

/**
 * Represents the current game been played. Implemented as a Singleton as only one game can be
 * loaded at a time. Data is read from the database on the first access and cached in memory. If
 * no data exists in the database, a random map and default settings are created and stored in the
 * database for persistence.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 *
 * https://stackoverflow.com/questions/54075649/access-application-context-in-companion-object-in-kotlin
 */
class Game {
    /** @property[Game.map] The map object */
    var map: GameMap

    /** @property[Game.settings] Default or persistant settings */
    val settings: Settings = Settings()

    /** @property[Game.money] Players overall cash */
    var money: Int = 0
        private set

    /** @property[Game.gameTime] Game ticks */
    var gameTime: Int = 0
        private set

    /** @property[Game.population] The number of people residing in the town */
    var population: Int = 0
        private set
        get() {return settings.familySize * 10}

    /** @property[Game.nResidential] The number of residential structures */
    var nResidential: Int = 0
        private set

    /** @property [Game.nCommercial] The number of commercial structures */
    var nCommercial: Int = 0
        private set

    init {
        // TODO: Check if a database object exists and load game from that

        // Placeholder map with one element to ensure map is never null
        map = GameMap(arrayOf(arrayOf(GameMap.MapElement(false, 0, 0, 0, 0))))
    }

    /**
     * Begins the game. If a new game, the map is created and stored in the database along with
     * the current settings.
     */
    fun start() {
        money = settings.initialMoney
        map = GameMap(MapData.generateGrid(settings.mapHeight, settings.mapWidth))
    }

    companion object {
        /** @property[get] Singleton object reference */
        val get = Game()
    }

    /**
     * Attempts to delete a structure from the map
     * @param[position] The position of the structure on the map
     */
    fun deleteStructure(position: Int) : Boolean {
        val element = map.get(position)
        var deleted = false

        if (element.structure is Road) {
            deleted = deleteRoad(position)
        }
        else {
            // Update structure totals
            when (element.structure) {
                is Residential -> nResidential--
                is Commercial -> nCommercial--
                else -> {/** do nothing */}
            }
            // Delete structure reference
            element.structure = null
            deleted = true
        }

        return deleted
    }

    /**
     * Checks if any buildings rely on a road at the given position
     * @param[position] THe position on the map
     */
    fun deleteRoad(position: Int) : Boolean {
        return deleteRoad(position / map.height, position % map.height)
    }

    /**
     * Checks if any buildings rely on a road at the given coordinates
     * @param[x] The x coordinate on the map
     * @param[y] The y coordinate on the map
     */
    fun deleteRoad(x: Int, y: Int) : Boolean {
        var safeDelete = true
        val element = map.get(x, y)

        if (element.structure is Road) {
            // Save a temporary copy of the structure incase undo required
            val road: Road = element.structure as Road

            // Delete the road then check if adjacent buildings are still supported by another road
            element.structure = null

            // North and South (Y axis) - Step by 2 to skip the element being deleted
            for (i in (y-1)..(y+1) step 2) {
                if (map.get(x, i).structure is Building) {
                    safeDelete = adjacentToRoad(x, i)
                }
                if (!safeDelete) break
            }
            // West and East (X axis)
            if (safeDelete) {
                for (i in (x-1)..(x+1) step 2) {
                    if (map.get(i, y).structure is Building) {
                        safeDelete = adjacentToRoad(i, y)
                    }
                    if (!safeDelete) break
                }
            }

            // If can't delete safely then revert the structure removal
            if (!safeDelete) {
                element.structure = road
            }
        }
        else {
            throw RuntimeException("Error deleting road as structure is not of type Road.")
        }

        return safeDelete
    }

    /**
     * Attempts to place a structure on the map. Returns true if successful
     * @param[structure] The structure to place
     * @param[position] The map position to place the structure
     */
    fun putStructure(structure: Structure, position: Int) : Boolean {
        val element = map.get(position)
        var placed = false

        // If can build here and nothing yet built
        if (element.buildable && element.structure == null) {
            // If building, ensure a road is adjacent
            if (structure is Building && adjacentToRoad(position)) {
                element.structure = structure
                // Update building totals
                when (structure is Residential) {
                    true -> nResidential++
                    false -> nCommercial++
                }
                placed = true
            }
            // Otherwise if not building then place it
            else if (structure !is Building) {
                element.structure = structure
                placed = true
            }
        }

        return placed
    }

    /**
     * Checks if a road is adjacent to a map position. Either North, east, south or west
     * @param[x] The x coordinate on the map
     * @param[y] The y coordinate on the map
     */
    fun adjacentToRoad(x: Int, y: Int) : Boolean {
        var isAdjacent: Boolean = false

        // North and South (Y axis) - Step by 2 to skip the element being placed on to
        for (i in (y-1)..(y+1) step 2) {
            isAdjacent = (i >= 0 && i < map.height && map.get(x, i).structure is Road)
            if (isAdjacent) break
        }
        // West and East (X axis) - only if no road found above
        if (!isAdjacent) {
            for (i in (x-1)..(x+1) step 2) {
                isAdjacent = (i >= 0 && i < map.width && map.get(i, y).structure is Road)
                if (isAdjacent) break
            }
        }

        return isAdjacent
    }

    /**
     * Checks if a road is adjacent to a map position. Either North, east, south or west
     * @param[position] The map position to check adjacency
     */
    fun adjacentToRoad(position: Int) : Boolean {
        return adjacentToRoad(position / map.height, position % map.height)
    }

    /**
     * Enumerator class to identify action mode when interacting with map
     */
    enum class EditMode {
        BUILD, DELETE, DETAILS
    }
}