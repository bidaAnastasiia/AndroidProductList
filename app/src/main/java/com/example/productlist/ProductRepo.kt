package com.example.productlist

import androidx.lifecycle.LiveData

class ProductRepo(private val productDao: ProductDao) {
    val allProducts: LiveData<List<Product>> = productDao.getProducts()
    suspend fun insert(product: Product):Long {
      return  productDao.insert(product)
    }

    suspend fun delete(product: Product){
        productDao.delete(product)
    }

    suspend fun deleteAll(){
        productDao.deleteAll()
    }

    suspend fun update(product: Product){
        productDao.update(product)
    }

    suspend fun findById(id:Long):Product{
        return productDao.findById(id)
    }
}