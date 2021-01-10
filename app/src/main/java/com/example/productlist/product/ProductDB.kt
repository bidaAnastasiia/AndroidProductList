package com.example.productlist.product

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class ProductDB: RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object{
        private var instance: ProductDB? = null

        fun getdatabase(context: Context): ProductDB {
            if(instance != null)
                return instance as ProductDB
            instance = Room.databaseBuilder(context.applicationContext,
                ProductDB::class.java,
                "ProductDatabase"
            ).build()
            return instance as ProductDB
        }
    }
}