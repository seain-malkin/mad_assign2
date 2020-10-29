package com.i19097842.curtin.edu.au.mad_assignment2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Argument constants for intent data
 */
private const val ARG_ROW = "com.i19097842.curtin.edu.au.mad_assignment2.details.row"
private const val ARG_COL = "com.i19097842.curtin.edu.au.mad_assignment2.details.col"
private const val ARG_TYPE = "com.i19097842.curtin.edu.au.mad_assignment2.details.type"
private const val ARG_NAME = "com.i19097842.curtin.edu.au.mad_assignment2.details.name"

class DetailsAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
    }

    companion object {
        /**
         * Creates an intent to launch the Details Activity
         * @param[row] The grid row number
         * @param[col] The grid column number
         * @param[type] The structure type
         * @param[name] The user defined name for the structure
         * @return The intent to inject into Details Activity
         */
        @JvmStatic
        fun getIntent(context: Context, row: Int, col: Int, type: String, name: String) : Intent {
            val intent = Intent(context, DetailsAct::class.java)
            intent.putExtra(ARG_ROW, row)
            intent.putExtra(ARG_COL, col)
            intent.putExtra(ARG_TYPE, type)
            intent.putExtra(ARG_NAME, name)

            return intent
        }
    }
}