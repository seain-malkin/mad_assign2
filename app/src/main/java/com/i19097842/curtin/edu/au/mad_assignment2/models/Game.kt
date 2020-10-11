package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.util.Log
import com.i19097842.curtin.edu.au.mad_assignment2.lib.MapData
import com.i19097842.curtin.edu.au.mad_assignment2.models.GameMap.MapElement

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
    val map: GameMap

    /** @property[Game.settings] Default or persistant settings */
    val settings: Settings = Settings()

    /** @property[Game.money] Players overall cash */
    var money: Int = 0
        private set

    /** @property[Game.gameTime] Game ticks */
    var gameTime: Int = 0
        private set

    init {
        map = GameMap(MapData.generateGrid(settings.mapHeight, settings.mapWidth))
    }

    companion object {
        /** @property[get] Singleton object reference */
        val get = Game()
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
     * @param[position] The map position to check adjacency
     */
    fun adjacentToRoad(position: Int) : Boolean {
        val x = position / map.height
        val y = position % map.height
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
}