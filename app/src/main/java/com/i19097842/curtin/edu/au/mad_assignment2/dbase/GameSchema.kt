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
     * All database tables extend this class. Ensures that every table has a name and
     * defined columns with at least the 'id' property
     */
    abstract class Table(val name: String) {
        abstract val cols: TableColumns
    }

    /**
     * Ensures all database tables has an 'id' column
     */
    abstract class TableColumns(val id: String = "id")

    /**
     * Table: Game
     */
    class GameTable : Table("game") {
        override val cols = GameTableColumns()

        class GameTableColumns : TableColumns() {
            val title: String = "title"
            val saveTime: String = "save_time"
        }
    }

    /**
     * Table: Settings
     */
    class SettingsTable : Table("settings"){
        override val cols = SettingsTableColumns()

        class SettingsTableColumns : TableColumns() {
            val gameId = "game_id"
            val mapWidth = "map_width"
            val mapHeight = "map_height"
            val initialMoney = "initial_money"
            val familySize = "family_size"
            val shopSize = "shop_size"
            val salary = "salary"
            val taxRate = "tax_rate"
            val serviceCost = "service_cost"
            val resiCost = "resi_cost"
            val commCost = "comm_cost"
            val roadCost = "road_cost"
            val treeCost = "tree_cost"
        }
    }

    /**
     * Table: Values
     */
    class ValuesTable : Table("values") {
        override val cols = ValuesTableColumns()

        class ValuesTableColumns : TableColumns() {
            val gameId = "game_id"
            val ticks = "ticks"
            val money = "money"
            val moneyChange = "money_change"
            val population = "population"
            val employmentRate = "employment_rate"
            val residential = "residential"
            val commercial = "commercial"
        }
    }
}