package com.example.vapesafe

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import kotlinx.android.synthetic.main.questionnaire_fragment.view.*

class Questionnaire : Fragment() {

    companion object {
        const val FILENAME = "VapeSafe_Settings"
        const val CHAR_OFFSET = 48
        const val EIGHT_TO_INT = 56
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.questionnaire_fragment, container, false)

        //Populating the spinners

        val spinnerq1: Spinner = view.spinner_q1
        createSpinner(spinnerq1, R.array.qa_q1_choices, view)
        val spinnerq2: Spinner = view.spinner_q2
        createSpinner(spinnerq2, R.array.qa_q2_choices, view)
        val spinnerq3: Spinner = view.spinner_q3
        createSpinner(spinnerq3, R.array.qa_q3_choices, view)
        val spinnerq4: Spinner = view.spinner_q4
        createSpinner(spinnerq4, R.array.qa_q4_choices, view)

        val submit = view.submitButton

        submit.setOnClickListener {
            val sharedPreferences: SharedPreferences = context?.getSharedPreferences(FILENAME, MODE_PRIVATE)!!
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString("q1", spinnerq1.selectedItem.toString())
            editor.putString("q2", spinnerq2.selectedItem.toString())
            editor.putString("q3", spinnerq3.selectedItem.toString())
            editor.putString("q4", spinnerq4.selectedItem.toString())

            editor.putString("recommended", calculateRecommendedLimit(spinnerq1.selectedItem.toString(),
                spinnerq2.selectedItem.toString(), spinnerq3.selectedItem.toString(),
                spinnerq4.selectedItem.toString()).toString() + " per day")

            editor.apply()

            getActivity()!!.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        return view
    }

    private fun createSpinner(sp: Spinner, resArray: Int, view: View): Unit {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            view.context,
            resArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            sp.adapter = adapter
        }
    }

    private fun calculateRecommendedLimit(s1: String, s2: String, s3: String, s4: String): Int {
        val q1 = (s1[0].toInt() / 6)
        val q2: Int

        when ((s2.substring(0, 3).toInt() - CHAR_OFFSET) >= 131) {
            true -> q2 = 2
            false -> q2 = 0
        }

        val q3 = ((s3.substring(0, 2).trim().toInt() - CHAR_OFFSET) / 5)

        val q4: Int

        when (s4[0].toInt() == EIGHT_TO_INT) {
            true -> q4 = 10
            false -> q4 = s4[5].toInt() - CHAR_OFFSET
        }
        return q4 - q1 - q2 - q3 - 2
    }

}
