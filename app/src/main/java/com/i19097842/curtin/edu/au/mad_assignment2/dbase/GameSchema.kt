package com.i19097842.curtin.edu.au.mad_assignment2.dbase

/**
 * Schema for the game database
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameSchema {
    /**
     * Contains static fields that reference the table inner classes
     */
    companion object  {
        @JvmStatic
        val game = GameTable()

        @JvmStatic
        val settings = SettingsTable()

        @JvmStatic
        val values = ValuesTable()
    }

    /**
     * Table: Game
     */
    class GameTable {
        val NAME = "game"
        val cols = Cols()

        class Cols {
            val ID = "id"
            val SAVE_TIME = "save_time"
        }
    }

    /**
     * Table: Settings
     */
    class SettingsTable {
        val NAME = "settings"
        val cols = Cols()

        class Cols {
            val ID = "id"
            val GAME_ID = "game_id"
            val MAP_WIDTH = "map_width"
            val MAP_HEIGHT = "map_height"
            val FAMILY_SIZE = "family_size"
            val SHOP_SIZE = "shop_size"
            val SALARY = "salary"
            val TAX_RATE = "tax_rate"
            val SERVICE_COST = "service_cost"
            val RESI_COST = "resi_cost"
            val COMM_COST = "comm_cost"
            val ROAD_COST = "road_cost"
            val TREE_COST = "tree_cost"
        }
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