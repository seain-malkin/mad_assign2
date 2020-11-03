package com.i19097842.curtin.edu.au.mad_assignment2.dbase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.i19097842.curtin.edu.au.mad_assignment2.GameApp

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
        private val instance = GameDbHelper(GameApp.getContext())
        val db = instance.writableDatabase
    }

    /**
     * Create the game database
     * @see[SQLiteOpenHelper.onCreate]
     */
    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            createValues(it)
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
     * Creates the Values table
     * @param[db] The database resource
     */
    private fun createValues(db: SQLiteDatabase) {
        val table = GameSchema.values
        db.execSQL("CREATE TABLE " + table.NAME + "("
                + table.cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + table.cols.GAME_ID + " INTEGER NOT NULL, "
                + table.cols.TICKS + " INTEGER NOT NULL, "
                + table.cols.MONEY + " INTEGER NOT NULL, "
                + table.cols.MONEY_CHANGE + " INTEGER NOT NULL, "
                + table.cols.POPULATION + " INTEGER NOT NULL, "
                + table.cols.EMPLOYMENT_RATE + " REAL NOT NULL, "
                + table.cols.RESIDENTIAL + " INTEGER NOT NULL, "
                + table.cols.COMMERCIAL + " INTEGER NOT NULL)")
    }
}