package com.example.productlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ProductViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: ProductRepo
    val allProducts: LiveData<List<Product>>

    init {
        val productDao: ProductDao = ProductDB.getdatabase(app).productDao()
        repo = ProductRepo(productDao)
        allProducts = repo.allProducts
    }

    fun insert(product: Product) = repo.insert(product)

    fun delete(product: Product) = repo.delete(product)

    fun deleteAll() = repo.deleteAll()

    fun update(product: Product) = repo.update(product)
}