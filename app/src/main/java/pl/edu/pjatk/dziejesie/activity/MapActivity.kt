package pl.edu.pjatk.dziejesie.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.edu.pjatk.dziejesie.R
import pl.edu.pjatk.dziejesie.databinding.ActivityMapBinding
import kotlin.concurrent.thread

const val RANGE = 500f

class MapActivity: AppCompatActivity(), OnMapReadyCallback {
    val db = Firebase.firestore
    lateinit var map: GoogleMap
    private val main = HandlerCompat.createAsync(Looper.getMainLooper())

    val mapFragment: SupportMapFragment
    get() = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

    private val view by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }
    var eventsLoc: List<LatLng> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        load()
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        mapFragment.getMapAsync(this)



    }

    override fun onMapReady(googleMap: GoogleMap) {
                map = googleMap
                if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    for(loc in eventsLoc){
                        map.addMarker(MarkerOptions().position(loc))
                        val circle = CircleOptions()
                            .strokeColor(Color.RED)
                            .radius(RANGE.toDouble())
                            .center(loc)
                            .strokeWidth(10f)
                        map.addCircle(circle)
                    }
                    map.isMyLocationEnabled = true
                } else {
                    requestPermissions(arrayOf(ACCESS_FINE_LOCATION), 1)
                }
                map.setOnMapClickListener { onClicked(it) }
            }

        private fun onClicked(latLng: LatLng) {
            val circle = CircleOptions()
                .strokeColor(Color.RED)
                .radius(RANGE.toDouble())
                .center(latLng)
                .strokeWidth(10f)
            map.addCircle(circle)
        }

        @SuppressLint("MissingPermission")
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    fun load() = thread {
        val db: FirebaseFirestore
        db = Firebase.firestore
        var eventslocs = mutableListOf<LatLng>()
        db.collection("events")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    eventslocs.add(stringToLocation(document.data.getValue("loc").toString()))
                }
                eventsLoc = eventslocs
            }
    }


    }
        fun stringToLocation(loc: String): LatLng {
            val latlong = loc.split(",".toRegex()).toTypedArray()
            val latitude = latlong[0].toDouble()
            val longitude = latlong[1].toDouble()
            var location = LatLng(latitude, longitude)
            return location
        }

