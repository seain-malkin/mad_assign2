package com.i19097842.curtin.edu.au.mad_assignment2.models

//TODO: Replace with settings object
const val HEIGHT= 10
const val WIDTH = 10


/**
 * Represents the current game been played. Implemented as a Singleton as only one game can be
 * loaded at a time. Data is read from the database on the first access and cached in memory. If
 * no data exists in the database, a random map and default settings are created and stored in the
 * database for persistence.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameData private constructor() {
    val map = GameMap(HEIGHT, WIDTH)
    var money: Int = 0
        private set
    var gameTime: Int = 0
        private set

    companion object {
        /**
         * @property[get] Singleton object reference
         */
        val get = GameData()
    }
}