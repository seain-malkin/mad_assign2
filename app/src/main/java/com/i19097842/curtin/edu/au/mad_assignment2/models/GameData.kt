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
    val map: GameMap
    val settings: Settings
    var money: Int = 0
        private set
    var gameTime: Int = 0
        private set

    init {
        settings = Settings()
        map = GameMap(MapData.generateGrid(settings.mapHeight, settings.mapWidth))
    }

    companion object {
        /**
         * @property[get] Singleton object reference
         */
        val get = GameData()
    }
}