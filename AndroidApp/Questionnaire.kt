package com.example.vapesafe

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.google.gson.Gson
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.questionnaire_fragment.view.*

class Questionnaire : Fragment() {

    companion object {
        const val FILENAME = "VapeSafe_Settings"
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

        var select1 = ""
        var select2 = ""
        var select3 = ""
        var select4 = ""

        spinnerq1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    select1 = parent.getItemAtPosition(position).toString()
                    Log.d("MainActivity", select1)
                }
            }

        }

        spinnerq2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    select2 = parent.getItemAtPosition(position).toString()
                }
            }

        }

        spinnerq3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    select3 = parent.getItemAtPosition(position).toString()
                }
            }

        }

        spinnerq4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    select4 = parent.getItemAtPosition(position).toString()
                }
            }

        }

        val clickListener = View.OnClickListener {
            val sharedPreferences: SharedPreferences = view.context.getSharedPreferences(FILENAME, MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString("q1", select1)
            editor.putString("q2", select2)
            editor.putString("q3", select3)
            editor.putString("q4", select4)
            editor.commit()

            Log.d("MainActivity", select1)
            Log.d("MainActivity", sharedPreferences.getString("q1", "").toString())

        }

        val submitButton: Button = Button(view.context)
        submitButton.setOnClickListener(clickListener)



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
