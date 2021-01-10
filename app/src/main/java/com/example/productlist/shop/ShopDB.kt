package com.example.productlist.shop

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Shop::class], version = 1)
abstract class ShopDB: RoomDatabase() {
    abstract fun shopDao(): ShopDao

    companion object{
        private var instance: ShopDB? = null

        fun getdatabase(context: Context): ShopDB {
            if(instance != null)
                return instance as ShopDB
            instance = Room.databaseBuilder(context.applicationContext,
                ShopDB::class.java,
                "ShopDatabase"
            ).build()
            return instance as ShopDB
        }
    }
}