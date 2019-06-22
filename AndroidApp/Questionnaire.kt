package com.example.vapesafe

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.questionnaire_fragment.view.*

class Questionnaire : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.questionnaire_fragment, container, false)

        val spinnerq1: Spinner = view.spinner_q1

        createSpinner(spinnerq1, R.array.qa_q1_choices, view)
        val spinnerq2: Spinner = view.spinner_q2
        createSpinner(spinnerq2, R.array.qa_q2_choices, view)
        val spinnerq3: Spinner = view.spinner_q3
        createSpinner(spinnerq3, R.array.qa_q3_choices, view)
        val spinnerq4: Spinner = view.spinner_q4 as Spinner
        createSpinner(spinnerq4, R.array.qa_q4_choices, view)

        return view
    }

    fun createSpinner(sp: Spinner, resArray: Int, view: View): Unit {

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

    //onButtonPress
        //Calculate the recommended limit
        //Send the limit to settings fragment
}
