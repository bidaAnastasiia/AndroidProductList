package com.example.productlist.shop

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopDao {
    @Query("SELECT * FROM Shop")
    fun getProducts(): LiveData<List<Shop>>

    @Insert
    suspend fun insert(shop: Shop): Long

    @Delete
    suspend fun delete(shop: Shop)

    @Query("DELETE FROM Shop")
    suspend fun deleteAll()

    @Update
    suspend fun update(shop: Shop)

    @Query("SELECT * FROM Shop WHERE id=:id")
    suspend fun findById(id:Long): Shop
}