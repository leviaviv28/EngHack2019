package com.example.vapesafe

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.home_fragment.view.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment: Fragment(){

    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    lateinit var barChart:BarChart
    lateinit var button_sync: Button
    lateinit var syncing_status: TextView

    lateinit var bluetoothAdapter:BluetoothAdapter
    lateinit var hc05: BluetoothDevice
    lateinit var bluetoothSocket: BluetoothSocket

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.home_fragment,container,false)

        initComponents(view)
        initBluetoothComponents()


        listenForSync()


        return view
    }

    private fun initBluetoothComponents(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        hc05 = bluetoothAdapter.getRemoteDevice("98:D3:32:21:5D:6C")
    }

    private fun initComponents(view:View){
        barChart = view.data_chart
        button_sync = view.button_sync
        syncing_status = view.SyncingStatus
    }

    private fun listenForSync() {
        button_sync.setOnClickListener {

            syncing_status.text = "Syncing..."

            //Check if bluetooth communication is set up
            if(!::bluetoothSocket.isInitialized){
                try {
                    bluetoothSocket = hc05.createRfcommSocketToServiceRecord(uuid)
                    bluetoothSocket.connect()

                    if(bluetoothSocket.isConnected){
                        Toast.makeText(context,"Connected to HC-05",Toast.LENGTH_LONG).show()
                    }
                }catch(e: IOException){
                    Toast.makeText(context,"Failed to connect HC-05",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }else{
                if(bluetoothSocket.isConnected){
                    Toast.makeText(context,"Already connected to HC-05",Toast.LENGTH_LONG).show()
                }
            }


            //Request Arduino For Syncing Operation
            try {
                //Send Request
                val outputStream = bluetoothSocket?.getOutputStream()
                outputStream?.write('S'.toInt())

                Thread.sleep(1000)

                val inputStream = bluetoothSocket?.inputStream
                var str = ""
                while(inputStream.available() > 0){
                    str += inputStream.read().toChar() + " "
                }

                syncing_status.text = str

            }catch (e:IOException){}

            //Check if the phone is connected to the HC05

        }

    }

    private fun plot() {
        val entires = ArrayList<BarEntry>()
        entires.add(BarEntry(2f,0))
        entires.add(BarEntry(4f,1))
        entires.add(BarEntry(1f,2))
        entires.add(BarEntry(6f,3))
        entires.add(BarEntry(8f,4))
        entires.add(BarEntry(7f,5))
        entires.add(BarEntry(9f,6))

        val bardataSet = BarDataSet(entires,"Amount Used")

        val labels = ArrayList<String>()

        labels.add("Sun")
        labels.add("Mon")
        labels.add("Tue")
        labels.add("Wed")
        labels.add("Thur")
        labels.add("Fri")
        labels.add("Sat")

//        button_sync.setOnClickListener {
//            val data = BarData(labels,bardataSet)
//            barChart.data = data
//            barChart.setDescription("")
//            barChart.animateY(1000)
//        }

    }
}