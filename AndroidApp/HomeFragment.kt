package com.example.vapesafe

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.home_fragment.view.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeFragment: Fragment(){

    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    lateinit var barChart:BarChart
    lateinit var button_sync: Button

    lateinit var bluetoothAdapter:BluetoothAdapter
    lateinit var hc05: BluetoothDevice
    lateinit var bluetoothSocket: BluetoothSocket

    var sevenDayData = TreeMap<Int,Int>() //Date, Value

    companion object{
        val FILENAME = "VapeSafe_Settings"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.home_fragment,container,false)

        initComponents(view)
        initBluetoothComponents()

        loadData()
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
    }

    private fun listenForSync() {
        button_sync.setOnClickListener {

            val currentDate = Calendar.getInstance()
            val date = currentDate.get(Calendar.DATE)
            val month = currentDate.get(Calendar.MONTH)
            val year = currentDate.get(Calendar.YEAR) % 100
            //val sendDate = "${year}${month}${date}"
            val byteArray = ByteArray(5)
            byteArray[0] = year.toByte()
            byteArray[1] = 44.toByte()
            byteArray[2] = month.toByte()
            byteArray[3] = 44.toByte()
            byteArray[4] = date.toByte()
            //Check if bluetooth communication is set up

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


            Thread.sleep(1000)

            //Request Arduino For Syncing Operation
            try {
                //Send Request
                val outputStream = bluetoothSocket.outputStream
                outputStream.write(byteArray)
                Log.d("HomeFragment","REQUESTING SYNC....")

                Thread.sleep(1000)

                val inputStream = bluetoothSocket.inputStream
                var str = ""
                while(inputStream.available() > 0){
                    str += inputStream.read().toChar()
                }

                Thread.sleep(1000)

                if(bluetoothSocket.isConnected) {
                    bluetoothSocket.close()
                    Toast.makeText(context, "Synced", Toast.LENGTH_SHORT).show()
                }

                if(str.isNotEmpty()){
                    Log.d("HomeFragment","SENDING DATA: ${date.toByte().toInt()}")
                    splitIncomingData(str)
                }

//                outputStream.close()
//                inputStream.close()

            }catch (e:IOException){
                Log.d("HomeFragment","ERROR DISCONNECTING")
            }


        }

    }

    private fun splitIncomingData(str: String) {
        val string = str.replace("\n","")
        val splitData = string.split("/").toTypedArray()
        val startDateArray = splitData.get(0).split(",").toTypedArray()
        val remainingDataArray = splitData.get(1).split(",").toTypedArray()

        val startDate = Calendar.getInstance()
        startDate.set(Calendar.YEAR, startDateArray.get(0).toInt()+2000)
        startDate.set(Calendar.MONTH, startDateArray.get(1).toInt())
        startDate.set(Calendar.DATE, startDateArray.get(2).toInt())

        var dateOffset = startDateArray.get(2).toInt()

        for (data in remainingDataArray){
            if(data.isEmpty())
                continue
            sevenDayData.put(dateOffset,data.toInt())
            Log.d("HomeFragment","${dateOffset}/${startDate.get(Calendar.MONTH)}/${startDate.get(Calendar.YEAR)} -> $data \n")
            dateOffset++
        }

        storeData()

    }

    private fun doesFileExist(): Boolean {
        val sharedPreferences: SharedPreferences = context?.getSharedPreferences(FILENAME, AppCompatActivity.MODE_PRIVATE)!!
        return sharedPreferences != null
    }

    private fun doesDataExist(key: String): Boolean {
        if (!doesFileExist())
            return false

        val sharedPreferences: SharedPreferences = context?.getSharedPreferences(FILENAME, AppCompatActivity.MODE_PRIVATE)!!
        val getMonthData = sharedPreferences.getString(key, null)
        return getMonthData != null
    }

    private fun storeData(){
        val sharedPreferences: SharedPreferences = context?.getSharedPreferences(FILENAME, AppCompatActivity.MODE_PRIVATE)!!
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()

        if(doesDataExist("June")){
            val jsonText = sharedPreferences.getString("June", null)
            val dataType = object : TypeToken<TreeMap<Int,Int>>() {}.type
            val thisMonthData = gson.fromJson<TreeMap<Int,Int>>(jsonText, dataType)

            for (keys in sevenDayData.keys){
                thisMonthData.put(keys,sevenDayData.getValue(keys))
            }
            //Add The Data
            val jsonData = gson.toJson(thisMonthData)
            editor.putString("June", jsonData)
            editor.apply()
            Toast.makeText(context, "APPENDING DATA", Toast.LENGTH_SHORT).show()
        }else{
            val jsonData = gson.toJson(sevenDayData)
            editor.putString("June", jsonData)
            editor.commit()
        }

        loadData()
    }

    private fun loadData(){
        if (doesDataExist("June")) {
            val sharedPreferences: SharedPreferences =
                context?.getSharedPreferences(FILENAME, AppCompatActivity.MODE_PRIVATE)!!
            val gson = Gson()
            val jsonText = sharedPreferences.getString("June", null)
            val dataType = object : TypeToken<TreeMap<Int,Int>>() {}.getType()
            val data = gson.fromJson<TreeMap<Int,Int>>(jsonText, dataType)

            val keys = ArrayList<Int>(data.keys)
            Log.d("HomeFragment","Key Size: ${keys.size}")
            var index = keys.size-1
            var limit = 7
            sevenDayData.clear()

            Log.d("HomeFragment","SevenDayData size is ${sevenDayData.size}")

            while (limit > 0){
                sevenDayData.put(keys.get(index),data.get(keys.get(index))!!)
                index--
                limit--
            }

            Log.d("HomeFragment","Graphing ${sevenDayData.size} data")

            plot()
        }
    }

    private fun plot() {

        val entires = ArrayList<BarEntry>()
        var index = 0
        for (keys in sevenDayData.keys){
            entires.add(BarEntry(sevenDayData.get(keys)?.toFloat()!!,index))
            index++
        }

        val bardataSet = BarDataSet(entires,"Amount Used")

        val labels = ArrayList<String>()
        val dateFormat = SimpleDateFormat("d/MMM")

        for (keys in sevenDayData.keys){
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DATE,keys)
            val format = dateFormat.format(calendar.time)
            labels.add(format)
        }


        val data = BarData(labels,bardataSet)
        barChart.data = data
        barChart.setDescription("")
        barChart.animateY(1000)

    }
}
