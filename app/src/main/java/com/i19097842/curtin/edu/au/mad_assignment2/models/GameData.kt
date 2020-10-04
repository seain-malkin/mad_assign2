package com.i19097842.curtin.edu.au.mad_assignment2.models

import com.i19097842.curtin.edu.au.mad_assignment2.lib.MapData

//TODO: Replace with settings object
const val HEIGHT = 10
const val WIDTH = 30


/**
 * Represents the current game been played. Implemented as a Singleton as only one game can be
 * loaded at a time. Data is read from the database on the first access and cached in memory. If
 * no data exists in the database, a random map and default settings are created and stored in the
 * database for persistence.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameData private constructor() {
    /** @property[GameData.map] The map object */
    val map: GameMap

    /** @property[GameData.settings] Default or persistant settings */
    val settings: Settings

    /** @property[GameData.money] Players overall cash */
    var money: Int = 0
        private set

    /** @property[GameData.gameTime] Game ticks */
    var gameTime: Int = 0
        private set

    init {
        settings = Settings()
        map = GameMap(MapData.generateGrid(settings.mapHeight, settings.mapWidth))
    }

    companion object {
        /** @property[get] Singleton object reference */
        val get = GameData()
    }
}