package com.tamara.a25b_11345b_pacmanrace.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.switchmaterial.SwitchMaterial
import com.tamara.a25b_11345b_pacmanrace.R
import com.tamara.a25b_11345b_pacmanrace.utilities.SignalManager

@SuppressLint("UseSwitchCompatOrMaterialCode")
class MainMenuActivity : AppCompatActivity() {

    private var btnStartGame: Button? = null
    private var btnViewScores: Button? = null
    private var switchSensorMovement: SwitchMaterial? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PREFS = "location_prefs"
    private val LOCATION_MODE_KEY = "location_mode"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        SignalManager.init(this)

        findViews()
        handleLocationPreference()
        initListeners()
    }

    private fun findViews() {
        btnStartGame = findViewById(R.id.btn_start_game)
        btnViewScores = findViewById(R.id.btn_view_scores)
        switchSensorMovement = findViewById(R.id.menu_SWITCH_tilt)
    }

    private fun initListeners() {
        btnStartGame?.setOnClickListener {
            val sensorEnabled = switchSensorMovement?.isChecked ?: false
            val selectedSpeedId = findViewById<RadioGroup>(R.id.menu_RG_speed_mode).checkedRadioButtonId

            val prefs = getSharedPreferences(LOCATION_PREFS, MODE_PRIVATE)
            val locationMode = prefs.getString(LOCATION_MODE_KEY, null)

            val handleStart = { isFast: Boolean?, lat: Double, lon: Double ->
                startGame(sensorEnabled, isFast, lat, lon)
            }

            val handleManualSpeed = { lat: Double, lon: Double ->
                showSpeedSelectionDialog { isFast ->
                    handleStart(isFast, lat, lon)
                }
            }

            if (locationMode == "never") {
                val lat = 0.0
                val lon = 0.0
                if (selectedSpeedId == R.id.menu_RB_manual_speed) {
                    handleManualSpeed(lat, lon)
                } else {
                    handleStart(null, lat, lon)
                }
            } else {
                if (!isLocationEnabled()) {
                    SignalManager.getInstance().toast("Please enable location services")
                    return@setOnClickListener
                }

                val permission = Manifest.permission.ACCESS_FINE_LOCATION
                if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    fetchCurrentLocationWithRetry { lat, lon ->
                        if (selectedSpeedId == R.id.menu_RB_manual_speed) {
                            handleManualSpeed(lat, lon)
                        } else {
                            handleStart(null, lat, lon)
                        }
                    }
                } else {
                    val lat = 0.0
                    val lon = 0.0
                    if (selectedSpeedId == R.id.menu_RB_manual_speed) {
                        handleManualSpeed(lat, lon)
                    } else {
                        handleStart(null, lat, lon)
                    }
                }
            }
        }

        btnViewScores?.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, HighScoresActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 101)
        }
    }

    private fun handleLocationPreference() {
        requestLocationPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocationWithRetry(onLocationReceived: (Double, Double) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    SignalManager.getInstance().toast("Using lastLocation")
                    onLocationReceived(location.latitude, location.longitude)
                } else {
                    fusedLocationClient.getCurrentLocation(
                        com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                        null
                    ).addOnSuccessListener { newLocation ->
                        if (newLocation != null) {
                            SignalManager.getInstance().toast("Using getCurrentLocation")
                            onLocationReceived(newLocation.latitude, newLocation.longitude)
                        } else {
                            SignalManager.getInstance().toast("Using fallback: Afeka")
                            onLocationReceived(32.114121, 34.817744)
                        }
                    }.addOnFailureListener {
                        SignalManager.getInstance().toast("Error → fallback: Afeka")
                        onLocationReceived(32.114121, 34.817744)
                    }
                }
            }
            .addOnFailureListener {
                SignalManager.getInstance().toast("LastLocation error → fallback: Afeka")
                onLocationReceived(32.114121, 34.817744)
            }
    }

    private fun startGame(sensorEnabled: Boolean, manualSpeedFast: Boolean?, lat: Double, lon: Double) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("EXTRA_SENSOR_ENABLED", sensorEnabled)
        intent.putExtra("EXTRA_LATITUDE", lat)
        intent.putExtra("EXTRA_LONGITUDE", lon)

        manualSpeedFast?.let {
            intent.putExtra("EXTRA_MANUAL_FAST_MODE", it)
        }

        startActivity(intent)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
    }

    private fun showSpeedSelectionDialog(onSpeedChosen: (Boolean) -> Unit) {
        val options = arrayOf("Fast", "Slow")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Speed Mode")
            .setItems(options) { _, which ->
                val isFast = (which == 0)
                onSpeedChosen(isFast)
            }
            .setCancelable(true)
            .show()
    }

}