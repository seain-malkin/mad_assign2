package com.i19097842.curtin.edu.au.mad_assignment2

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

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

        val row = intent.getIntExtra(ARG_ROW, 0)
        val col = intent.getIntExtra(ARG_COL, 0)


        // Find view elements
        val coordTv = findViewById<TextView>(R.id.detailsCoord)
        val typeTv = findViewById<TextView>(R.id.detailsType)
        val nameEt = findViewById<EditText>(R.id.detailsName)
        val saveBtn = findViewById<Button>(R.id.detailsActionSave)
        val cancelBtn = findViewById<Button>(R.id.detailsActionCancel)

        // Update elements
        coordTv.setText(resources.getString(R.string.details_coord, row, col))
        typeTv.setText(resources.getString(R.string.details_type, intent.getStringExtra(ARG_TYPE)))
        nameEt.setText(intent.getStringExtra(ARG_NAME))

        // Set button events
        saveBtn.setOnClickListener {
            val details = Intent()
            details.putExtra(ARG_ROW, row)
            details.putExtra(ARG_COL, col)
            details.putExtra(ARG_NAME, nameEt.text.toString())

            setResult(Activity.RESULT_OK, details)
            finish()
        }

        cancelBtn.setOnClickListener{ finish() }
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

        fun getRow(intent: Intent, default: Int = 0) : Int {
            return intent.getIntExtra(ARG_ROW, default)
        }

        fun getCol(intent: Intent, default: Int = 0) : Int {
            return intent.getIntExtra(ARG_COL, default)
        }

        fun getName(intent: Intent) : String? {
            return intent.getStringExtra(ARG_NAME)
        }
    }
}