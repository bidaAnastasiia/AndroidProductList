package com.example.productlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productlist.databinding.ActivityShopListBinding
import com.example.productlist.shop.Shop
import com.example.productlist.shop.ShopAdapter
import com.example.productlist.shop.ShopViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.shoplist_element.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_shop_list)
        val binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvShop.layoutManager = LinearLayoutManager(baseContext)
        binding.rvShop.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                DividerItemDecoration.VERTICAL
            )
        )
        val shopViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ShopViewModel::class.java)
        shopViewModel.allShops.observe(this, Observer{ shops ->
            shops?.let{
                (binding.rvShop.adapter as ShopAdapter).setShops(it)
            }
        })
        binding.rvShop.adapter = ShopAdapter(this,shopViewModel)

        val latc = intent.getParcelableExtra<LatLng>("latlng")?.latitude
        val lngc = intent.getParcelableExtra<LatLng>("latlng")?.longitude

        binding.buttonAddShop.setOnClickListener {
                val shop = Shop(
                    name = binding.etShopName.text.toString(),
                    description = binding.etShopDescription.text.toString(),
                    radius = binding.etShopRadius.text.toString().toDouble(),
                    lat = latc!!,
                    lng = lngc!!
                )
            Log.d("SHOPLAT", shop.lat.toString())
                shopViewModel.insert(shop)
            }


    }
}