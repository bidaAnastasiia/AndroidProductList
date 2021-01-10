package com.example.productlist.shop

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.productlist.product.Product
import com.example.productlist.product.ProductDB
import com.example.productlist.product.ProductDao
import com.example.productlist.product.ProductRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class ShopViewModel (app: Application) : AndroidViewModel(app) {

    private val repo: ShopRepo
    val allShops: LiveData<List<Shop>>

    init {
        val shopDao: ShopDao = ShopDB.getdatabase(app).shopDao()
        repo = ShopRepo(shopDao)
        allShops = repo.allShops
    }

    fun insert(shop:Shop) :Long= runBlocking { return@runBlocking repo.insert(shop) }


    fun delete(shop:Shop) {
        CoroutineScope(IO).launch { repo.delete(shop)}
    }

    fun deleteAll() {
        CoroutineScope(IO).launch { repo.deleteAll()}
    }

    fun update(shop:Shop){
        CoroutineScope(IO).launch { repo.update(shop)}
    }

    fun findById(id:Long) : Shop = runBlocking { return@runBlocking repo.findById(id) }

}