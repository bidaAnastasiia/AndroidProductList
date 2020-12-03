package com.example.productlist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    fun getProducts(): LiveData<List<Product>>

    @Insert
    suspend fun insert(product: Product): Long

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM Product")
    suspend fun deleteAll()

    @Update
    suspend fun update(product: Product)

    @Query("SELECT * FROM Product WHERE id=:id")
    suspend fun findById(id:Long): Product

}