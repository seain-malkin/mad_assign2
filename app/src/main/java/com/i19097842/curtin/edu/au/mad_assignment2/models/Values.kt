package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.content.ContentValues
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
     * Secondary constructor. Loads values from persistent storage on initialisation
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
     * Saves the object to persistent storage
     * @param[gameId] The database gameId
     */
    fun save(gameId: Int) {
        // Create a data object to pass to the database helper
        val cv = ContentValues()
        GameSchema.values.cols.let {
            cv.put(it.gameId, gameId)
            cv.put(it.ticks, ticks)
            cv.put(it.money, money)
            cv.put(it.moneyChange, moneyChange)
            cv.put(it.population, population)
            cv.put(it.employmentRate, employmentRate)
            cv.put(it.residential, nResidential)
            cv.put(it.commercial, nCommercial)
        }

        GameDbHelper.instance.save(GameSchema.values, cv, id)
    }

    /**
     * Sets all class properties from database storage associated with the given game id
     * @param[gameId] The game id to search for
     */
    private fun load(gameId: Int) {
        val cols = GameSchema.values.cols

        // Retrieve row from persistent storage.
        GameDbHelper.instance.open(GameSchema.values, cols.id, gameId).run {
            // Point to first and only result and update object properties
            moveToFirst()
            id = getInt(getColumnIndex(cols.id))
            ticks = getInt(getColumnIndex(cols.ticks))
            money = getInt(getColumnIndex(cols.money))
            moneyChange = getInt(getColumnIndex(cols.moneyChange))
            population = getInt(getColumnIndex(cols.population))
            employmentRate = getDouble(getColumnIndex(cols.employmentRate))
            nResidential = getInt(getColumnIndex(cols.residential))
            nCommercial = getInt(getColumnIndex(cols.commercial))

            // Delete reference to result
            close()
        }
    }
}