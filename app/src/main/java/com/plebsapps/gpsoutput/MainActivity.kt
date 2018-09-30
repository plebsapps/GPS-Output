package com.plebsapps.gpsoutput

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var secondCounter : Int = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        onGps()
        onNet()
    }

    private fun onNet(){
        // Acquire a reference to the system Location Manager
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Define a listener that responds to location updates
        val locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0f, locationListener)
    }

    private fun onGps(){
        // Acquire a reference to the system Location Manager
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Define a listener that responds to location updates
        val locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f, locationListener)
    }

    private fun makeUseOfNewLocation(location: Location) {
        val lng = String.format("%.6f", location.longitude)
        val lat = String.format("%.6f", location.latitude)
        val alt = String.format("%.2f", location.altitude)
        val acc = String.format("%.2f", location.accuracy)
        val bea :String

        if (location.bearing != 0f) {
            bea = String.format("%.2f", location.bearing)
            tv_bearing_value.text = bea
        }

        secondCounter=0

        tv_actual_value.text = Date().formatToServerTimeDefaults()

        tv_latidude_value.text = lat
        tv_longitude_value.text = lng
        tv_altitude_value.text = alt
        tv_accuracy_value.text = acc
        tv_provider_value.text = location.provider
    }

    private fun Date.formatToServerTimeDefaults(): String{
        val sdf= SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(this)
    }

    //Set timer 1 sec
    private val runnable = object : Runnable {
        override fun run() {

            secondCounter++

            tv_lastupdate_value.text = String.format(getString(R.string.gps_update), secondCounter.toString())
            handler.postDelayed(this, 1000)
        }
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, 100)
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), Const.AskPermissionRequestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Const.AskPermissionRequestCode -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d(Const.TAG, "Permission has been denied by user")
                } else {
                    Log.d(Const.TAG, "Permission has been granted by user")
                }
            }
        }
    }
}