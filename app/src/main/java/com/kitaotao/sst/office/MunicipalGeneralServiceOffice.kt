package com.kitaotao.sst.office

import addSeasonalBackground
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.BaseActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.services.generalService.gso_ex_service_1
import com.kitaotao.sst.services.generalService.gso_int_ex_service_1
import com.kitaotao.sst.services.generalService.gso_int_ex_service_2
import com.kitaotao.sst.services.generalService.gso_int_ex_service_3
import com.kitaotao.sst.services.generalService.gso_int_service_1
import com.kitaotao.sst.services.generalService.gso_int_service_2
import com.kitaotao.sst.services.generalService.gso_int_service_3
import com.kitaotao.sst.services.generalService.gso_int_service_4
import com.kitaotao.sst.services.generalService.gso_int_service_5
import com.kitaotao.sst.services.generalService.gso_int_service_6
import com.kitaotao.sst.services.generalService.gso_int_service_7
import com.kitaotao.sst.setDynamicHeader
import isDeviceTabletClickPop
import officeViewChange
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import showClickPopAnimation

class MunicipalGeneralServiceOffice : BaseActivity() {

    private lateinit var overlayImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_municipal_general_service_office)

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
        val firstMarkerPoint = GeoPoint(7.640163, 125.013145) // First marker position
        val secondMarkerPoint = GeoPoint(7.640047, 125.008539) // Second marker position

// Add the first marker
        val firstMarker = Marker(mapView)
        firstMarker.position = firstMarkerPoint
        firstMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        firstMarker.title = "Municipal General Service Office"
        firstMarker.icon = ContextCompat.getDrawable(this, R.drawable.red_marker)
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
        secondMarker.title = "Municipal Hall"
        secondMarker.icon = ContextCompat.getDrawable(this, R.drawable.red_marker)
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
        overlayImage.setImageResource(R.drawable.gso256)  // Replace with your image resource

        // Set up the overlay image functionality
        setupOverlayImage(mapView, overlayImage)

        // Apply rounded corners without an image
        applyRoundedCorners(overlayImage)

        // Set click listeners for various services
        setClickListener(R.id.ex_service_1, gso_ex_service_1::class.java)
        setClickListener(R.id.int_service_1, gso_int_service_1::class.java)
        setClickListener(R.id.int_service_2, gso_int_service_2::class.java)
        setClickListener(R.id.int_service_3, gso_int_service_3::class.java)
        setClickListener(R.id.int_service_4, gso_int_service_4::class.java)
        setClickListener(R.id.int_service_5, gso_int_service_5::class.java)
        setClickListener(R.id.int_service_6, gso_int_service_6::class.java)
        setClickListener(R.id.int_service_7, gso_int_service_7::class.java)
        setClickListener(R.id.int_ex_service_1, gso_int_ex_service_1::class.java)
        setClickListener(R.id.int_ex_service_2, gso_int_ex_service_2::class.java)
        setClickListener(R.id.int_ex_service_3, gso_int_ex_service_3::class.java)
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
