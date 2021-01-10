package com.example.productlist.shop

import androidx.lifecycle.LiveData

class ShopRepo (private val shopDao: ShopDao) {

    val allShops: LiveData<List<Shop>> = shopDao.getProducts()
    suspend fun insert(shop: Shop):Long {
        return  shopDao.insert(shop)
    }

    suspend fun delete(shop: Shop){
        shopDao.delete(shop)
    }

    suspend fun deleteAll(){
        shopDao.deleteAll()
    }

    suspend fun update(shop: Shop){
        shopDao.update(shop)
    }

    suspend fun findById(id:Long): Shop {
        return shopDao.findById(id)
    }
}