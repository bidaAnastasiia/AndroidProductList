package com.example.productlist

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.productlist.shop.Shop
import com.example.productlist.shop.ShopAdapter
import com.example.productlist.shop.ShopViewModel
import com.google.android.gms.location.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object ShopsAccess{
        private lateinit var shopsAccess:List<Shop>

        fun saveShops(shopss:List<Shop>){
            this.shopsAccess = shopss
        }

        fun getShops(): List<Shop> {
            return shopsAccess
        }
    }

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private var shops = emptyList<Shop>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geoClient: GeofencingClient
    private lateinit var geo : Geofence
    var pinetent = Intent()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        geoClient = LocationServices.getGeofencingClient(this)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                mMap.addMarker(MarkerOptions().position(latLng).title("You are here"))
                Log.i("geofences","Your location: ${currentLocation.latitude.toString()} lng: ${currentLocation.longitude.toString()}")
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val shopViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ShopViewModel::class.java)
        shopViewModel.allShops.observe(this, Observer{
            setShops(it)
        })

    }

    fun setShops(shops:List<Shop>){
        this.shops = shops
        Log.d("SETSHOPS", "START")
        saveShops(shops)
//        pinetent = Intent(this, GeofenceBroadcastReceiver::class.java)
        for(shop in this.shops){
            val ll = LatLng(shop.lat,shop.lng)
            mMap.addMarker(MarkerOptions().position(ll).title(shop.name))
            geo = Geofence.Builder()
                    .setRequestId(shop.id.toString())
                    .setCircularRegion(
                        shop.lat,
                        shop.lng,
                        shop.radius.toFloat()
                    )
                    .setExpirationDuration(60*60*100)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build()
            pinetent = Intent(this,GeofenceBroadcastReceiver::class.java)
            pinetent.putExtra("shopName",shop.name)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            geoClient.addGeofences(getGeofencingRequest(geo), getGeofencePendingIntent())
                .addOnSuccessListener {
                    Log.i("geofences","Geofence dodany ${shop.id}")
                }
                .addOnFailureListener{
                    Toast.makeText(this@MapsActivity, "Geofence nie został dodany",Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun goToShopList(view: View){
        val intent = Intent(this, ShopListActivity::class.java)
        intent.putExtra("latlng", LatLng(currentLocation.latitude, currentLocation.longitude));
        startActivity(intent)
    }

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest{
        return GeofencingRequest.Builder().setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()
    }
    private fun getGeofencePendingIntent():PendingIntent{
        return PendingIntent.getBroadcast(
            this,
            0,
            pinetent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

}