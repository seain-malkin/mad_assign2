package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameDbHelper
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameSchema
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*
import kotlin.math.min

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
    /**
     * Singleton reference
     */
    companion object {
        /** @property[get] Singleton object reference */
        val get = Game()
    }

    /**
     * Enumerator class to identify action mode when interacting with map
     */
    enum class EditMode {
        BUILD, DELETE, DETAILS
    }

    /** @property[Game.id] The database id for this game */
    var id: Int = -1
        private set

    /** The name of the town */
    var title: String = "Newtownsville"

    /** @property[Game.map] The map object */
    lateinit var map: GameMap

    /** @property[Game.settings] Default or persistant settings */
    lateinit var settings: Settings

    /** @property[Game.values] Game related variables that change with the game */
    lateinit var values: Values

    lateinit var dbHelper: GameDbHelper

    /**
     * Injects the context and loads data from the database
     * @param[context] The activity or application context
     */
    fun load(context: Context) {
        // If dbHelper is already initialised then the game is already loaded
        if (!::dbHelper.isInitialized) {
            dbHelper = GameDbHelper(context)
            initGameData()
        }
    }

    /**
     * If a game exists, loads data from database. Otherwise initialises new data
     */
    private fun initGameData() {
        val table = GameSchema.game

        // Get the game with the most recent save time
        dbHelper.db().query(
            table.name,
            arrayOf(table.cols.id, table.cols.title),
            null, null, null, null,
            table.cols.saveTime + " DESC",
            "1"
        ).run {
            if (count == 1) {
                // Setup the game from the database result
                moveToFirst()
                id = getInt(getColumnIndex(table.cols.id))
                title = getString(getColumnIndex(table.cols.title))
                settings = Settings(dbHelper, id)
                values = Values(dbHelper, id)
                map = GameMap(dbHelper, settings.mapWidth, settings.mapHeight, id)
            } else {
                // No previous game. Initialise a new game
                settings = Settings(dbHelper)
                values = Values(dbHelper)

                // save newly initialised game to database
                save()
            }

            close()
        }
    }

    /**
     * Checks if the game has been started.
     * If the map has been created then it has.
     */
    fun inProgress() : Boolean {
        return ::map.isInitialized
    }

    /**
     * Ensures the map has been created.
     */
    fun start() {
        // Setup the map if not already
        if (!inProgress()) {
            values.addMoney(settings.initialMoney)
            values.save(id)
            map = GameMap(dbHelper, settings.mapWidth, settings.mapHeight, id)
        }
    }

    /**
     * Saves all game data to persistent storage
     */
    fun save() {
        val cv = ContentValues()
        GameSchema.game.cols.let {
            cv.put(it.title, title)
            cv.put(it.saveTime, Date().time.toInt())
        }

        // Save game data and get row id
        id = dbHelper.save(GameSchema.game, cv, if (id == -1) null else id)
    }

    /**
     * Runs game logic required for each game tick. Firstly all game values are re-calculated then
     * the win or loss conditions are checked
     */
    fun tick() {
        // Progress game time
        values.ticks++
        calculateGameValues()
        values.save(id)

        //TODO: Check for loss condition
        //TODO: Check for win condition
    }

    /**
     * Calculates the new game values
     */
    private fun calculateGameValues() {
        // Calculate: population
        values.population = values.nResidential * settings.familySize

        // Calculate: Employment Rate
        if (values.population == 0) {
            values.employmentRate = 1.0
        } else {
            values.employmentRate = min(
                1.0,
                (values.nCommercial * settings.shopSize / values.population.toDouble())
            )
        }

        // Calculate: Revenue
        values.adjustMoney((values.population * (
                (values.employmentRate * settings.salary * settings.taxRate)
                        - settings.serviceCost)).toInt())
    }

    /**
     * Attempts to delete a structure from the map
     * @param[position] The position of the structure on the map
     */
    fun deleteStructure(position: Int) : Boolean {
        val element = map.get(position)
        var deleted = true

        if (element.structure is Road) {
            deleted = deleteRoad(position)
        }
        else {
            // Update structure totals
            when (element.structure) {
                is Residential -> values.nResidential--
                is Commercial -> values.nCommercial--
                else -> {/** do nothing */}
            }
            // Update values database
            values.save(id)
            // Delete structure reference
            element.structure = null
        }

        // Save changes to database
        if (deleted) map.saveMapElement(id, element)

        return deleted
    }

    /**
     * Checks if any buildings rely on a road at the given position
     * @param[position] THe position on the map
     */
    private fun deleteRoad(position: Int) : Boolean {
        return deleteRoad(position / map.height, position % map.height)
    }

    /**
     * Checks if any buildings rely on a road at the given coordinates
     * @param[x] The x coordinate on the map
     * @param[y] The y coordinate on the map
     */
    private fun deleteRoad(x: Int, y: Int) : Boolean {
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

        // If can afford to build, can build here and nothing yet built
        if (canAffordStructure(structure) && element.buildable && element.structure == null) {
            // If building, ensure a road is adjacent
            if (structure is Building && adjacentToRoad(position)) {
                element.structure = structure.clone()
                // Update building totals
                when (structure is Residential) {
                    true -> values.nResidential++
                    false -> values.nCommercial++
                }
                placed = true
            }
            // Otherwise if not building then place it
            else if (structure !is Building) {
                element.structure = structure.clone()
                placed = true
            }

            // apply building costs and save to database
            if (placed) {
                applyBuildingCosts(structure)
                values.save(id)
                map.saveMapElement(id, element)
            }
        }

        return placed
    }

    private fun canAffordStructure(structure: Structure): Boolean {
        return when(structure) {
            is Residential -> values.money > settings.resiCost
            is Commercial -> values.money > settings.commCost
            is Road -> values.money > settings.roadCost
            is Tree -> values.money > settings.treeCost
            else -> true
        }
    }

    private fun applyBuildingCosts(structure: Structure) {
        when (structure) {
            is Residential -> values.subtractMoney(settings.resiCost)
            is Commercial -> values.subtractMoney(settings.commCost)
            is Road -> values.subtractMoney(settings.roadCost)
            is Tree -> values.subtractMoney(settings.treeCost)
        }
    }

    /**
     * Checks if a road is adjacent to a map position. Either North, east, south or west
     * @param[x] The x coordinate on the map
     * @param[y] The y coordinate on the map
     */
    private fun adjacentToRoad(x: Int, y: Int) : Boolean {
        var isAdjacent = false

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
    private fun adjacentToRoad(position: Int) : Boolean {
        return adjacentToRoad(position / map.height, position % map.height)
    }
}