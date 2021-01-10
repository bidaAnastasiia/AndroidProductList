package com.example.productlist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.productlist.shop.*
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private var shops = emptyList<Shop>()

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("geofence","onReceive")
        val geoEvent = GeofencingEvent.fromIntent(intent)
        val triggering = geoEvent.triggeringGeofences

        val shops:List<Shop> = MapsActivity.getShops()
        Log.d("geofence", shops[0].name)

        val service = Intent(context,MyService::class.java)
        service.putExtra("channel_id","channel_Geo")
        service.putExtra("channel_name","channel_GeoFence")
        service.putExtra("shopName",intent.getStringExtra("shopName"))
        service.putExtra("sale",(5..50).random().toString())
        service.putExtra("id",intent.getIntExtra("id", 0))

        for(geo in triggering){
            Log.i("geofence","Geofence with id: ${geo.requestId} is active")
            for (shop in shops){
                if(shop.id.toString() == geo.requestId)
                {
                    service.putExtra("shopName", shop.name)
                    break
                }
            }
        }
        if(geoEvent.geofenceTransition==Geofence.GEOFENCE_TRANSITION_ENTER){
            Log.i("geofences","Enter: ${geoEvent.triggeringLocation.toString()}")
            context.startService(service)
        } else{
            Log.e("geofence", "Error")
        }
    }
}