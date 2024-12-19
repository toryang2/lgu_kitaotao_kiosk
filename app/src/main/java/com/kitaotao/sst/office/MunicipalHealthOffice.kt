package com.kitaotao.sst.office

import addSeasonalBackground
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.BaseActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.services.mho.*
import com.kitaotao.sst.setDynamicHeader
import isDeviceTabletClickPop
import officeViewChange
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import showClickPopAnimation

class MunicipalHealthOffice : BaseActivity() {

    lateinit var mapView: MapView
    private lateinit var overlayImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_municipal_health_office)

        addSeasonalBackground()

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the map
        mapView = findViewById(R.id.mapView)  // Ensure your layout has a MapView
        initializeMap(mapView)

// Clear cache to ensure the new tiles load
        mapView.tileProvider.clearTileCache()

// Define GeoPoints for the markers
        val firstMarkerPoint = GeoPoint(7.641048, 125.008975) // First marker position
        val secondMarkerPoint = GeoPoint(7.640047, 125.008539) // Second marker position

// Add the first marker
        val firstMarker = Marker(mapView)
        firstMarker.position = firstMarkerPoint
        firstMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        firstMarker.title = "Municipal Health Office"
        firstMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.red_marker))
        mapView.overlays.add(firstMarker)

        firstMarker.infoWindow = object : InfoWindow(R.layout.bonuspack_bubble, mapView) {
            override fun onOpen(item: Any?) {
                val marker = item as Marker
                val titleTextView = mView.findViewById<TextView>(R.id.infoWindowTitle)
                titleTextView.text = marker.title
            }

            override fun onClose() {
                // Optional: actions when closing the window
            }
        }

// Add the second marker
        val secondMarker = Marker(mapView)
        secondMarker.position = secondMarkerPoint
        secondMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        secondMarker.title = "Municipality of Kitaotao"
        secondMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.red_marker))
        mapView.overlays.add(secondMarker)

        secondMarker.infoWindow = object : InfoWindow(R.layout.bonuspack_bubble, mapView) {
            override fun onOpen(item: Any?) {
                val marker = item as Marker
                val titleTextView = mView.findViewById<TextView>(R.id.infoWindowTitle)
                titleTextView.text = marker.title
            }

            override fun onClose() {
                // Optional: actions when closing the window
            }
        }

        adjustMapViewForMarkers(mapView, firstMarker, secondMarker)

// Optional: Display info windows
        firstMarker.showInfoWindow()
        secondMarker.showInfoWindow()

        val floorIDTextView = findViewById<TextView>(R.id.floorID)

        floorIDTextView.visibility = View.GONE

        // Set the overlay image resource here
        overlayImage = findViewById(R.id.overlayImage) // Ensure you have an ImageView in your layout with this ID
        overlayImage.setImageResource(R.drawable.mho256)  // Replace with your image resource

        // Set up the overlay image functionality
        setupOverlayImage(mapView, overlayImage)

        // Apply rounded corners without an image
        applyRoundedCorners(overlayImage)

        // Set click listeners for various services
        setClickListener(R.id.service_1, mho_service_1::class.java)
        setClickListener(R.id.service_2, mho_service_2::class.java)
        setClickListener(R.id.service_3, mho_service_3::class.java)
        setClickListener(R.id.service_4, mho_service_4::class.java)
        setClickListener(R.id.service_5, mho_service_5::class.java)
        setClickListener(R.id.service_6, mho_service_6::class.java)
        setClickListener(R.id.service_7, mho_service_7::class.java)
        setClickListener(R.id.service_8, mho_service_8::class.java)
        setClickListener(R.id.service_9, mho_service_9::class.java)
        setClickListener(R.id.service_10, mho_service_10::class.java)
        setClickListener(R.id.service_11, mho_service_11::class.java)
        setClickListener(R.id.service_12, mho_service_12::class.java)
        setClickListener(R.id.service_13, mho_service_13::class.java)
        setClickListener(R.id.service_14, mho_service_14::class.java)
        setClickListener(R.id.service_15, mho_service_15::class.java)
        setClickListener(R.id.service_16, mho_service_16::class.java)

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (isDeviceTabletClickPop()) {
            showClickPopAnimation(event) // Call the function defined in clickPop.kt
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume() // Required for MapView lifecycle
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause() // Required for MapView lifecycle
    }

    private fun setClickListener(viewId: Int, activityClass: Class<*>) {
        findViewById<TextView>(viewId).setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
