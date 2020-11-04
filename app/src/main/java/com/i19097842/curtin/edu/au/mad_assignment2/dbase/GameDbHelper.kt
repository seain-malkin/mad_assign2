package com.i19097842.curtin.edu.au.mad_assignment2.dbase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.i19097842.curtin.edu.au.mad_assignment2.GameApp
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameSchema.Table


private const val DATABASE_NAME = "madass2.db"
private const val VERSION = 1

/**
 * Handles Database interactions.
 * Implemented as a Singleton. The database resource should be
 * accessed from the companion object rather than instantiating
 * a new object
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameDbHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    /**
     * Singleton reference
     */
    companion object {
        val instance = GameDbHelper(GameApp.getContext())
        val db = instance.writableDatabase
    }

    /**
     * Create the game database
     * @see[SQLiteOpenHelper.onCreate]
     */
    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            createGameTable(it)
            createSettingsTable(it)
            createValuesTable(it)
        }
    }

    /**
     * If schema changed, update database
     * @see[SQLiteOpenHelper.onUpgrade]
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    /**
     * Either updates the row or inserts a new one
     * @param[table] The table object
     * @param[values] The data to insert
     * @param[rowId] The row id. Set to null for new entry
     * @return The affected row id. -1 is returned if nothing was changed
     */
    fun save(table: Table, values: ContentValues, rowId: Int? = null) : Int {
        // Either insert or update depending if the database id is already set
        val rowAffected = if (rowId == null) {
            // Insert and retrieve the new row id
            db.insert(table.name, null, values).toInt()
        } else {
            // Update the existing values row
            db.update(table.name, values, table.cols.id + " = ?",
                arrayOf(rowId.toString()))
        }

        // Return -1 on error or the rowId that was inserted or updated
        return when(rowAffected) { -1 -> -1 else -> rowId ?: rowAffected }
    }

    /**
     * Wrapper for a database select statement. Returns all columns of the table
     * @param[table] The table to search
     * @param[idKey] The name of the primary or foreign key
     * @param[idValue] The value of [idKey]
     * @return The database result as a cursor
     */
    fun open(table: Table, idKey: String, idValue: Int) : Cursor {
        return db.query(
            table.name,
            null,
            "$idKey = ?",
            arrayOf(idValue.toString()),
            null, null, null)
    }

    /**
     * Creates the Game table
     * @param[db] The database resource
     */
    private fun createGameTable(db: SQLiteDatabase) {
        val table = GameSchema.game
        db.execSQL("CREATE TABLE " + table.name + "("
                + table.cols.id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + table.cols.title + " TEXT"
                + table.cols.saveTime + " INTEGER NOT NULL)")
    }

    /**
     * Creates the Settings table
     * @param[db] The database resource
     */
    private fun createSettingsTable(db: SQLiteDatabase) {
        val table = GameSchema.settings
        db.execSQL("CREATE TABLE " + table.name + "("
                + table.cols.id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + table.cols.gameId + " INTEGER NOT NULL, "
                + table.cols.mapWidth + " INTEGER NOT NULL, "
                + table.cols.mapHeight + " INTEGER NOT NULL, "
                + table.cols.initialMoney + " INTEGER NOT NULL, "
                + table.cols.familySize + " INTEGER NOT NULL, "
                + table.cols.shopSize + " INTEGER NOT NULL, "
                + table.cols.salary + " INTEGER NOT NULL, "
                + table.cols.taxRate + " REAL NOT NULL, "
                + table.cols.serviceCost + " INTEGER NOT NULL, "
                + table.cols.resiCost + " INTEGER NOT NULL, "
                + table.cols.commCost + " INTEGER NOT NULL, "
                + table.cols.roadCost + " INTEGER NOT NULL, "
                + table.cols.treeCost + " INTEGER NOT NULL)")
    }

    /**
     * Creates the Values table
     * @param[db] The database resource
     */
    private fun createValuesTable(db: SQLiteDatabase) {
        val table = GameSchema.values
        db.execSQL("CREATE TABLE " + table.name + "("
                + table.cols.id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + table.cols.gameId + " INTEGER NOT NULL, "
                + table.cols.ticks + " INTEGER NOT NULL, "
                + table.cols.money + " INTEGER NOT NULL, "
                + table.cols.moneyChange + " INTEGER NOT NULL, "
                + table.cols.population + " INTEGER NOT NULL, "
                + table.cols.employmentRate + " REAL NOT NULL, "
                + table.cols.residential + " INTEGER NOT NULL, "
                + table.cols.commercial + " INTEGER NOT NULL)")
    }
}