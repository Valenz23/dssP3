package com.example.practica3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapActivity : ComponentActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Inicializar la configuración de OSMDroid
        Configuration.getInstance().load(this, this.getPreferences(MODE_PRIVATE))

        // Inicializar MapView
        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)  // Usamos el mapa estándar de OSMDroid
        mapView.setMultiTouchControls(true)  // Activar desplazamiento y zoom multitáctil

        // Centrar el mapa en una ubicación predeterminada
        val startPoint = GeoPoint(37.182955, -3.602178)
        val mapController = mapView.controller
        mapController.setZoom(14.0)
        mapController.setCenter(startPoint)

        // Marcadores
        addMarker(37.214126, -3.618242, "Almacén Granaita", "Este es el almacén de Granaita.")
        addMarker(37.146535, -3.612395, "Almacén Nevada", "Este es el almacén de Nevada.")
        addMarker(37.171625, -3.603122, "Almacén Recogidas", "Este es el almacén de Recogidas.")

        backToCatalog()
    }

    private fun backToCatalog() {
        val buttonBackToMain: Button = findViewById(R.id.buttonBackToCatalogMap)
        buttonBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun addMarker(latitude: Double, longitude: Double, title: String, snippet: String) {
        val geoPoint = GeoPoint(latitude, longitude)
        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.title = title
        marker.snippet = snippet

        mapView.overlays.add(marker)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
