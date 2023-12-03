package com.sgo.SmyrnaGlobalOutreach

import android.app.Dialog
import android.graphics.Rect
import android.location.GpsStatus
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.sgo.SmyrnaGlobalOutreach.databinding.ActivityContactUsBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class ContactUs : AppCompatActivity(), IMyLocationProvider, MapListener, GpsStatus.Listener {

    // variables
    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController
    private lateinit var mMyLocationNewOverlay: MyLocationNewOverlay
    private lateinit var controller: IMapController

    // declare global varibles for latitude and lagitude
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("Open Street Map Android", MODE_PRIVATE)
        )

        // map
        mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.mapCenter
        mapView.setMultiTouchControls(true)
        mapView.getLocalVisibleRect(Rect())

        mMyLocationNewOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
        controller = mapView.controller
        mMyLocationNewOverlay.enableMyLocation()
        mMyLocationNewOverlay.enableFollowLocation()
        mMyLocationNewOverlay.isDrawAccuracyEnabled = true

        // set the initial zoom level
        controller.setZoom(6.0)

        mapView.overlays.add(mMyLocationNewOverlay)
        setupMap() // ths function adds the marker fot the start point
        mapView.addMapListener(this)



       /* // Create a custom overlay for the animated marker
        val animatedMarkerOverlay = object : Overlay(this){
            override fun onSingleTapConfirmed(e: MotionEvent, mapView: MapView): Boolean {
                //calculate the longitude and latitude from the GeoPint
                val geoPoint = mMyLocationNewOverlay.myLocation
                latitude = geoPoint.latitude
                longitude = geoPoint.longitude

                return true
            }
        }
        //add the animatedMarlerOverlay to the map
        mapView.overlays.add(animatedMarkerOverlay)*/

    }


    private fun setupMap() {
        Configuration.getInstance().load(this,
            PreferenceManager.getDefaultSharedPreferences(this))
        // mapView = binding.mapView
        mapController = mapView.controller
        mapView.setMultiTouchControls(true)

        //Initialize the map with a start point
        val startPoint = GeoPoint(-29.72782, 30.59308)
        mapController.setCenter(startPoint)
        mapController.setZoom(6.0)

        //Create a marker for the start point (ic)
        val marker = Marker(mapView)
        marker.position = startPoint
        marker.icon = ResourcesCompat.getDrawable(resources,R.drawable.baseline_add_location_24, null)

        // add a click listener to the marker
        marker.setOnMarkerClickListener { marker, mapView ->
            val latitude = marker.position.latitude
            val logitude = marker.position.longitude

            val dialog = Dialog(this@ContactUs)
            dialog.setContentView(R.layout.custom)


            val latitudeTextView = dialog.findViewById<TextView>(R.id.latitudeTextView)
            val longitudeTextView = dialog.findViewById<TextView>(R.id.longitudeTextView)
            val addressTextView = dialog.findViewById<TextView>(R.id.AddressTextView)

            addressTextView.text = "Address: Thornridge Farm, 1 Alice Goswell Rd, Cato Ridge, 3680"
            latitudeTextView.text = "Latitude: $latitude"
            longitudeTextView.text = "Longitude: $logitude"

            dialog.show()

            true
        }

        mapView.overlays.add(marker)
    }

    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
        return true
    }

    override fun stopLocationProvider() {
        TODO("Not yet implemented")
    }

    override fun getLastKnownLocation(): Location {
        return Location("last_Known_location")
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return false
    }

    override fun onGpsStatusChanged(p0: Int) {
        TODO("Not yet implemented")
    }
}