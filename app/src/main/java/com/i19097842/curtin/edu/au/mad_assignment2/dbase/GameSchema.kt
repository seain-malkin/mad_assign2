package com.i19097842.curtin.edu.au.mad_assignment2.dbase

/**
 * Schema for the game database
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameSchema {
    companion object  {
        @JvmStatic
        val values = ValuesTable()
    }

    /**
     * Table: Values
     */
    class ValuesTable {
        // Table name
        val NAME = "values"
        val cols = Cols()

        // Table columns
        class Cols {
            val ID = "id"
            val GAME_ID = "game_id"
            val TICKS = "ticks"
            val MONEY = "money"
            val MONEY_CHANGE = "money_change"
            val POPULATION = "population"
            val EMPLOYMENT_RATE = "employment_rate"
            val RESIDENTIAL = "residential"
            val COMMERCIAL = "commercial"
        }
    }
}