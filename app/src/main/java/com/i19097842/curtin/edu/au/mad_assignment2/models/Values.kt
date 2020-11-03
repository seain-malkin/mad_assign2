package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.content.ContentValues
import android.database.Cursor
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameDbHelper
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameSchema

/**
 * Stores all changeable values related to the game and methods to
 * update them in a restricted manner.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class Values() {
    /**
     * Secondary constructor. Loads values from persistant storage on initialisation
     * @param[gameId] The game id the values are associated with
     */
    constructor(gameId: Int) : this() {
        load(gameId)
    }

    /** @property[id] The database id for this object */
    var id: Int? = null
        private set

    /** @property[ticks] The number of game ticks that have occurred */
    var ticks: Int = 0

    /** @property[money] The players current amount of money */
    var money: Int = 0
        private set

    /**
     * @property[moneyChange] The amount the money changed last tick
     */
    var moneyChange: Int = 0
        private set

    /** @property[population] The current population in the game  */
    var population: Int = 0

    /** @property[employmentRate] */
    var employmentRate: Double = 1.0

    /** @property[nResidential] The number of residential structures */
    var nResidential: Int = 0

    /** @property [nCommercial] The number of commercial structures */
    var nCommercial: Int = 0

    /**
     * Changes the money value
     * @param[adjustment] Positive or negative amount to adjust overall money by
     */
    fun adjustMoney(adjustment: Int) {
        moneyChange = adjustment
        money += adjustment
    }

    /**
     * Saves the object to persistant storage
     * @param[gameId] The database gameId
     */
    fun save(gameId: Int) {
        ContentValues().run {
            GameSchema.values.cols.let {
                put(it.GAME_ID, gameId)
                put(it.TICKS, ticks)
                put(it.MONEY, money)
                put(it.MONEY_CHANGE, moneyChange)
                put(it.POPULATION, population)
                put(it.EMPLOYMENT_RATE, employmentRate)
                put(it.RESIDENTIAL, nResidential)
                put(it.COMMERCIAL, nCommercial)
            }

            // Either insert or update depending if the database id is already set
            if (id == null) {
                // Insert and retrieve the new row id
                id = GameDbHelper.db.insert(
                    GameSchema.values.NAME,
                    null,
                    this).toInt()

                // Throw an exception if not inserted
                if (id == -1) {
                    throw IllegalStateException("Could not insert a new row into the values table")
                }
            }
            else {
                // Update the existing values row
                val affectedRows = GameDbHelper.db.update(
                    GameSchema.values.NAME,
                    this,
                    GameSchema.values.cols.ID + " = ?",
                    arrayOf(id.toString()))

                // Throw an exception if none or many rows updated
                if (affectedRows != 1) {
                    throw IllegalStateException("Could not update values table with id $id")
                }
            }
        }
    }

    /**
     * Sets all class properties from database storage associated with the given game id
     * @param[gameId] The game id to search for
     */
    private fun load(gameId: Int) {
        // Shorthand column reference
        val cols = GameSchema.values.cols

        // Define a cursor for query result
        var cursor: Cursor? = null

        // Attempt to retrieve database results
        try {
            cursor = GameDbHelper.db.rawQuery(
                "SELECT * FROM " + GameSchema.values.NAME
                    + " WHERE game_id = ?", arrayOf(gameId.toString()))

            // If row found, set object properties from result
            if (cursor.count > 0) {
                cursor.run {
                    moveToFirst()
                    // Set property values
                    id = getInt(getColumnIndex(cols.ID))
                    ticks = getInt(getColumnIndex(cols.TICKS))
                    money = getInt(getColumnIndex(cols.MONEY))
                    moneyChange = getInt(getColumnIndex(cols.MONEY_CHANGE))
                    population = getInt(getColumnIndex(cols.POPULATION))
                    employmentRate = getDouble(getColumnIndex(cols.EMPLOYMENT_RATE))
                    nResidential = getInt(getColumnIndex(cols.RESIDENTIAL))
                    nCommercial = getInt(getColumnIndex(cols.COMMERCIAL))
                }
            }
            else {
                // Could not load from the given game id; throw exception
                throw IllegalStateException("Could not load values for game id $gameId.")
            }
        }
        finally {
            cursor?.close()
        }
    }
}