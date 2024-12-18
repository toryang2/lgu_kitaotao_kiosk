package com.kitaotao.sst.office

import addSeasonalBackground
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.BaseActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.services.tourism.*
import com.kitaotao.sst.setDynamicHeader
import isDeviceTabletClickPop
import officeViewChange
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import showClickPopAnimation

class TOURISM : BaseActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_tourism)

        addSeasonalBackground()

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mapView = findViewById(R.id.mapView)

// Set the OpenTopoMap tile source (terrain map)
        mapView.setTileSource(TileSourceFactory.OpenTopo)

// Adjust the map center to be slightly below the original center
        val originalCenter = GeoPoint(7.639444, 125.008618)
        val adjustedCenter = GeoPoint(originalCenter.latitude + 0.0005, originalCenter.longitude) // Shift slightly below

// Setup map with the adjusted position
        MapUtility.setupMap(mapView, adjustedCenter, 19.0) // Example: Slightly shifted position

        // Add the CompassOverlay
        val compassOverlay = CompassOverlay(this, mapView)
        compassOverlay.enableCompass()  // Enable compass functionality
        mapView.overlays.add(compassOverlay)

// First Marker
        val firstMarkerPoint = GeoPoint(7.639531, 125.008597) // First marker position
        val firstMarker = Marker(mapView)
        firstMarker.position = firstMarkerPoint
        firstMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        firstMarker.title = "Municipal Tourism Office" // Title for the first marker
        mapView.overlays.add(firstMarker)

        firstMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.red_marker))

// Custom info window for the first marker
        firstMarker.infoWindow = object : InfoWindow(R.layout.bonuspack_bubble, mapView) {
            override fun onOpen(item: Any?) {
                val marker = item as Marker

                // Find the TextViews in the custom layout
                val titleTextView = mView.findViewById<TextView>(R.id.infoWindowTitle)

                // Set the marker's title and description to the TextViews
                titleTextView.text = marker.title // Set the title of the marker
            }

            override fun onClose() {
                // Optional: actions when closing the window
            }
        }
        firstMarker.showInfoWindow()

// Second Marker
        val secondMarkerPoint = GeoPoint(7.640138, 125.008442) // Second marker position
        val secondMarker = Marker(mapView)
        secondMarker.position = secondMarkerPoint
        secondMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        secondMarker.title = "Municipality of Kitaotao" // Title for the second marker
        mapView.overlays.add(secondMarker)

        secondMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.red_marker))

// Custom info window for the second marker
        secondMarker.infoWindow = object : InfoWindow(R.layout.bonuspack_bubble, mapView) {
            override fun onOpen(item: Any?) {
                val marker = item as Marker

                // Find the TextViews in the custom layout
                val titleTextView = mView.findViewById<TextView>(R.id.infoWindowTitle)

                // Set the marker's title and description to the TextViews
                titleTextView.text = marker.title // Set the title of the marker
            }

            override fun onClose() {
                // Optional: actions when closing the window
            }
        }
        secondMarker.showInfoWindow()


        // Set click listeners for various services
        setClickListener(R.id.in_service_1, tourism_in_service_1::class.java)
        setClickListener(R.id.in_ex_service_1, tourism_in_ex_service_1::class.java)
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
