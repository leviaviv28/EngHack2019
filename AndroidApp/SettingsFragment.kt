package com.example.vapesafe

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.settings_fragment.view.*

const val FILENAME = "VapeSafe_Settings"

class SettingsFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.settings_fragment,container,false)

        setTextView(view.Height, "q1")
        setTextView(view.Weight,"q2")
        setTextView(view.BeenSmokingFor, "q3")
        setTextView(view.SmokesPerDay, "q4")
        setTextView(view.Recommended, "recommended")

        view.submitData.setOnClickListener {
            val sharedPreferences: SharedPreferences = context?.getSharedPreferences(FILENAME, MODE_PRIVATE)!!
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString("limit", view.LimitPerDay.editableText.toString() + " per day")
            editor.apply()
        }

        view.recalculate.setOnClickListener {
            getActivity()!!.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, Questionnaire())
                .commit()
        }

        view.ResetData.setOnClickListener {
            //Clear
        }

        return view
    }

    private fun setTextView(textView: TextView, key: String): Unit {
        val sharedPreferences: SharedPreferences = context?.getSharedPreferences(FILENAME, MODE_PRIVATE)!!
        textView.text = sharedPreferences.getString(key, "")
    }
}