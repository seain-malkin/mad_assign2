package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.content.ContentValues
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameDbHelper
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameSchema

/**
 * Settings of the game in play with default values. Persistent settings will overwrite.
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class Settings() {

    /**
     * Secondary constructor. Loads settings from persistent storage on initialisation
     * @param[gameId] The game id the values are associated with
     */
    constructor(gameId: Int) : this() {
        load(gameId)
    }

    /** @property[id] The database id for these settings */
    var id: Int? = null

    /** @property[mapWidth] Width of the map  */
    var mapWidth = 50

    /** @property[mapHeight] Height of the map  */
    var mapHeight = 10

    /** @property[initialMoney] The amount of money at the start of the game  */
    var initialMoney = 1000

    /** @property[familySize] The number of people in each home  */
    var familySize = 4

    /** @property[shopSize] The number of people that can be employed in a shop  */
    var shopSize = 6

    /** @property[salary] The cost for each employee  */
    var salary = 10

    /** @property[taxRate] The rate that employees are taxed  */
    var taxRate = 0.3

    /** @property[serviceCost] The cost incurred to service each citizen   */
    var serviceCost = 2

    /** @property[resiCost] The cost to build a residential structure */
    var resiCost = 100

    /** @property[commCost] The cost to build a commercial structure  */
    var commCost = 500

    /** @property[roadCost] The cost to build a road structure */
    var roadCost = 500

    /** @property[treeCost] The cost to build a tree structure  */
    var treeCost = 10

    /**
     * Saves the object to persistent storage
     * @param[gameId] The database gameId
     */
    fun save(gameId: Int) {
        // Create a data object to pass to the database helper
        val cv = ContentValues()
        GameSchema.settings.cols.let {
            cv.put(it.gameId, gameId)
            cv.put(it.mapWidth, mapWidth)
            cv.put(it.mapHeight, mapHeight)
            cv.put(it.initialMoney, initialMoney)
            cv.put(it.familySize, familySize)
            cv.put(it.shopSize, shopSize)
            cv.put(it.salary, salary)
            cv.put(it.taxRate, taxRate)
            cv.put(it.serviceCost, serviceCost)
            cv.put(it.resiCost, resiCost)
            cv.put(it.commCost, commCost)
            cv.put(it.roadCost, roadCost)
            cv.put(it.treeCost, treeCost)
        }

        GameDbHelper.instance.save(GameSchema.values, cv, id)
    }

    /**
     * Sets all class properties from database storage associated with the given game id
     * @param[gameId] The game id to search for
     */
    private fun load(gameId: Int) {
        val cols = GameSchema.settings.cols

        // Retrieve row from persistent storage.
        GameDbHelper.instance.open(GameSchema.settings, cols.id, gameId).run {
            // Point to first and only result and update object properties
            moveToFirst()
            id = getInt(getColumnIndex(cols.id))
            mapWidth = getInt(getColumnIndex(cols.mapWidth))
            mapHeight = getInt(getColumnIndex(cols.mapHeight))
            initialMoney = getInt(getColumnIndex(cols.initialMoney))
            familySize = getInt(getColumnIndex(cols.familySize))
            shopSize = getInt(getColumnIndex(cols.shopSize))
            salary = getInt(getColumnIndex(cols.salary))
            taxRate = getDouble(getColumnIndex(cols.taxRate))
            serviceCost = getInt(getColumnIndex(cols.serviceCost))
            resiCost = getInt(getColumnIndex(cols.resiCost))
            commCost = getInt(getColumnIndex(cols.commCost))
            roadCost = getInt(getColumnIndex(cols.roadCost))
            treeCost = getInt(getColumnIndex(cols.treeCost))

            // Delete reference to result
            close()
        }
    }
}