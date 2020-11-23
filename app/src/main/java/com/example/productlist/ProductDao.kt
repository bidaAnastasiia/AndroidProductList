package com.example.productlist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    fun getProducts(): LiveData<List<Product>>

    @Insert
    fun insert(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("DELETE FROM Product")
    fun deleteAll()

    @Update
    fun update(product: Product)
}