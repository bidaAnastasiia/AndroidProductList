package com.example.productlist.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class ProductViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: ProductRepo
    val allProducts: LiveData<List<Product>>

    init {
        val productDao: ProductDao = ProductDB.getdatabase(app).productDao()
        repo = ProductRepo(productDao)
        allProducts = repo.allProducts
    }

     fun insert(product: Product) :Long= runBlocking { return@runBlocking repo.insert(product) }


     fun delete(product: Product) {
         CoroutineScope(IO).launch { repo.delete(product)}
    }

     fun deleteAll() {
         CoroutineScope(IO).launch { repo.deleteAll()}
    }

     fun update(product: Product){
         CoroutineScope(IO).launch { repo.update(product)}
    }

     fun findById(id:Long) : Product = runBlocking { return@runBlocking repo.findById(id) }


}