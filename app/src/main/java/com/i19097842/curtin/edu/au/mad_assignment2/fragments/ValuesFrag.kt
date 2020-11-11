package com.i19097842.curtin.edu.au.mad_assignment2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.i19097842.curtin.edu.au.mad_assignment2.R
import com.i19097842.curtin.edu.au.mad_assignment2.dbase.GameSchema.Companion.game
import com.i19097842.curtin.edu.au.mad_assignment2.models.Game
import kotlin.math.abs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ValuesFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class ValuesFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var moneyTv: TextView
    private lateinit var moneyChangeTv: TextView
    private lateinit var popTv: TextView
    private lateinit var emplRateTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_values, container, false)
        val v = Game.get.values

        // Find view elements
        moneyTv = view.findViewById(R.id.valuesMoney)
        moneyChangeTv = view.findViewById(R.id.valuesMoneyChange)
        popTv = view.findViewById(R.id.valuesPopulation)
        emplRateTv = view.findViewById(R.id.valuesEmplRate)

        // Update view elements
        update(
            money = v.money,
            moneyChange = v.moneyChange,
            pop = v.population,
            emplRate = v.employmentRate)

        return view
    }

    /**
     * Updates the UI elements
     */
    fun update(
        money: Int? = null, moneyChange: Int? = null, pop: Int? = null, emplRate: Double? = null)
    {
        val v = Game.get.values
        val sign: String = if (v.moneyChange < 0) "-" else "+"

        // Only update values that have been set
        money?.let{ moneyTv.setText(getString(R.string.values_money, it)) }
        moneyChange?.let{
            moneyChangeTv.setText(getString(R.string.values_money_change, sign, abs(it)))
        }
        pop?.let{ popTv.setText(getString(R.string.values_population, it)) }
        emplRate?.let{
            emplRateTv.setText(
                getString(R.string.values_empl_rate, (v.employmentRate * 100).toInt()))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ValuesFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String = "", param2: String = "") =
            ValuesFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}