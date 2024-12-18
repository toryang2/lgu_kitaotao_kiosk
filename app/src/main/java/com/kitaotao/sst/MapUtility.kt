import android.content.Context
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

object MapUtility {

    // Initialize osmdroid configuration
    fun initialize(context: Context) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    }

    // Setup MapView with a specific GeoPoint
    fun setupMap(mapView: MapView, geoPoint: GeoPoint, zoomLevel: Double = 15.0): MapView {
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        val mapController = mapView.controller
        mapController.setZoom(zoomLevel)
        mapController.setCenter(geoPoint)

        return mapView
    }

    // Add a marker to the MapView
    fun addMarker(mapView: MapView, geoPoint: GeoPoint, title: String = "Marker"): Marker {
        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = title
        mapView.overlays.add(marker)
        return marker
    }
}
