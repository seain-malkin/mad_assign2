package com.i19097842.curtin.edu.au.mad_assignment2.models

/**
 * Represents the current game been played. Implemented as a Singleton as only one game can be
 * loaded at a time. Data is read from the database on the first access and cached in memory. If
 * no data exists in the database, a random map and default settings are created and stored in the
 * database for persistence.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameData private constructor() {



    companion object {
        /**
         * @property[instance] Singleton object reference
         */
        val instance = GameData()
    }
}