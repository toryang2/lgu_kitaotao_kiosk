package com.kitaotao.sst.office

import addSeasonalBackground
import android.content.Intent
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.BaseActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.services.tourism.*
import com.kitaotao.sst.setDynamicHeader
import isDeviceTabletClickPop
import officeViewChange
import org.json.JSONObject
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import showClickPopAnimation
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.floor

class TOURISM : BaseActivity() {

    private lateinit var overlayImage: ImageView

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

        // Initialize the map
        mapView = findViewById(R.id.mapView)  // Ensure your layout has a MapView
        initializeMap(mapView)

// Clear cache to ensure the new tiles load
        mapView.tileProvider.clearTileCache()

// Define GeoPoints for the markers
        val firstMarkerPoint = GeoPoint(7.639452, 125.008603) // First marker position
        val secondMarkerPoint = GeoPoint(7.640047, 125.008539) // Second marker position

// Add the first marker
        val firstMarker = Marker(mapView)
        firstMarker.position = firstMarkerPoint
        firstMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        firstMarker.title = "Municipal Tourism Office"
        firstMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.red_marker))
        mapView.overlays.add(firstMarker)

        firstMarker.infoWindow = object : InfoWindow(R.layout.bonuspack_bubble, mapView) {
            override fun onOpen(item: Any?) {
                val marker = item as Marker
                //val linearLayout = mView.findViewById<LinearLayout>(R.id.infoWindowLayout) // Root layout
                val titleTextView = mView.findViewById<TextView>(R.id.infoWindowTitle)
                titleTextView.text = marker.title

                //startBreathingAnimation(linearLayout)
            }

            override fun onClose() {
                // Optional: actions when closing the window
            }
        }

// Add the second marker
        val secondMarker = Marker(mapView)
        secondMarker.position = secondMarkerPoint
        secondMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        secondMarker.title = "Municipal Hall"
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

        // Fetch PolyLine
        fetchAndDisplayPolyline(firstMarkerPoint, secondMarkerPoint)

        val floorIDTextView = findViewById<TextView>(R.id.floorID)

        floorIDTextView.visibility = View.GONE

        // Set the overlay image resource here
        overlayImage = findViewById(R.id.overlayImage) // Ensure you have an ImageView in your layout with this ID
        overlayImage.setImageResource(R.drawable.tourism256)  // Replace with your image resource

        // Set up the overlay image functionality
        setupOverlayImage(mapView, overlayImage)

        // Apply rounded corners without an image
        applyRoundedCorners(overlayImage)


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
