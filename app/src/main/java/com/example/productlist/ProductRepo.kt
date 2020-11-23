package com.example.productlist

import androidx.lifecycle.LiveData

class ProductRepo(private val productDao: ProductDao) {
    val allProducts: LiveData<List<Product>> = productDao.getProducts()
    fun insert(product: Product){
        productDao.insert(product)
    }

    fun delete(product: Product){
        productDao.delete(product)
    }

    fun deleteAll(){
        productDao.deleteAll()
    }

    fun update(product: Product){
        productDao.update(product)
    }
}