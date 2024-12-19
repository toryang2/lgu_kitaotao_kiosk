package com.kitaotao.sst.office

import addSeasonalBackground
import android.content.Intent
import android.graphics.DashPathEffect
import android.graphics.Paint
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
import com.kitaotao.sst.services.liga.*
import com.kitaotao.sst.setDynamicHeader
import isDeviceTabletClickPop
import officeViewChange
import org.json.JSONObject
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.InfoWindow
import showClickPopAnimation
import java.net.HttpURLConnection
import java.net.URL

class LIGA : BaseActivity() {

    lateinit var mapView: MapView
    private lateinit var overlayImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_liga)

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
        val firstMarkerPoint = GeoPoint(7.638611, 125.008107) // First marker position
        val secondMarkerPoint = GeoPoint(7.640047, 125.008539) // Second marker position

// Add the first marker
        val firstMarker = Marker(mapView)
        firstMarker.position = firstMarkerPoint
        firstMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        firstMarker.title = "LIGA ng Barangay"
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

        // Call the method from BaseActivity to get the URL
        val urlString = createGraphHopperUrl(firstMarkerPoint, secondMarkerPoint)

        Thread {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val response = connection.inputStream.bufferedReader().readText()

                val jsonResponse = JSONObject(response)
                val paths = jsonResponse.getJSONArray("paths")
                if (paths.length() > 0) {
                    val route = paths.getJSONObject(0)

                    // Decode the polyline string
                    val encodedPolyline = route.getString("points")
                    val decodedPoints = decodePolyline(encodedPolyline)

                    // Create a Polyline for the map
                    val polyline = Polyline(mapView)
                    for (geoPoint in decodedPoints) {
                        polyline.addPoint(geoPoint)
                    }

                    // Customize the polyline appearance
                    val paint = polyline.outlinePaint
                    paint.color = ContextCompat.getColor(this, R.color.red)
                    paint.strokeWidth = 10f
                    paint.style = Paint.Style.STROKE
                    paint.strokeCap = Paint.Cap.ROUND // Set the line ends to be rounded
                    paint.strokeJoin = Paint.Join.ROUND

                    // Check if the last decoded point is not the destination point
                    val lastDecodedPoint = decodedPoints.last()
                    val firstDecodedPoint = decodedPoints.first()

                    // If the first decoded point is different from the destination, add the missing route
                    if (firstDecodedPoint != firstMarkerPoint) {
                        val missingRoutePolyline = Polyline(mapView)
                        missingRoutePolyline.addPoint(firstDecodedPoint)
                        missingRoutePolyline.addPoint(firstMarkerPoint)

                        // Customize the missing route polyline appearance (dashed line for the missing route)
                        val missingRoutePaint = missingRoutePolyline.outlinePaint
                        missingRoutePaint.color = ContextCompat.getColor(this, R.color.red)
                        missingRoutePaint.strokeWidth = 8f
                        missingRoutePaint.style = Paint.Style.STROKE
                        missingRoutePaint.strokeCap = Paint.Cap.ROUND
                        missingRoutePaint.strokeJoin = Paint.Join.ROUND
                        missingRoutePaint.pathEffect = DashPathEffect(floatArrayOf(5f, 15f), 0f) // Dashed line

                        // Add the missing route polyline to the map
                        runOnUiThread {
                            mapView.overlays.add(missingRoutePolyline)
                        }
                    }


                    // If the last decoded point is different from the destination, add the missing route
                    if (lastDecodedPoint != secondMarkerPoint) {
                        val missingRoutePolyline = Polyline(mapView)
                        missingRoutePolyline.addPoint(lastDecodedPoint)
                        missingRoutePolyline.addPoint(secondMarkerPoint)

                        // Customize the missing route polyline appearance (dashed line for the missing route)
                        val missingRoutePaint = missingRoutePolyline.outlinePaint
                        missingRoutePaint.color = ContextCompat.getColor(this, R.color.red)
                        missingRoutePaint.strokeWidth = 8f
                        missingRoutePaint.style = Paint.Style.STROKE
                        missingRoutePaint.strokeCap = Paint.Cap.ROUND
                        missingRoutePaint.strokeJoin = Paint.Join.ROUND
                        missingRoutePaint.pathEffect = DashPathEffect(floatArrayOf(5f, 15f), 0f) // Dashed line

                        // Add the missing route polyline to the map
                        runOnUiThread {
                            mapView.overlays.add(missingRoutePolyline)
                        }
                    }

                    // Add the main route polyline to the map
                    runOnUiThread {
                        mapView.overlays.add(polyline)
                        mapView.invalidate()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()

        val floorIDTextView = findViewById<TextView>(R.id.floorID)

        floorIDTextView.visibility = View.GONE

        // Set the overlay image resource here
        overlayImage = findViewById(R.id.overlayImage) // Ensure you have an ImageView in your layout with this ID
        overlayImage.setImageResource(R.drawable.liga256)  // Replace with your image resource

        // Set up the overlay image functionality
        setupOverlayImage(mapView, overlayImage)

        // Apply rounded corners without an image
        applyRoundedCorners(overlayImage)

        // Set click listeners for various services
        setClickListener(R.id.service_1, liga_service_1::class.java)
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
