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

class Questionnaire : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.questionnaire_fragment, container, false)



        return view
    }
    
    //onButtonPress
        //Calculate the recommended limit
        //Send the limit to settings fragment
}
