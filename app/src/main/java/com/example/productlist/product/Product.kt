package com.example.productlist.product

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Product(@PrimaryKey(autoGenerate = true) var id: Long = 0,
              var name: String,
              var price: String,
              var amount: Int,
              var isBought:Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )


//    fun fromContentValues(values: ContentValues?){
//        var product :Product
//        if (values != null) {
//            if(values.containsKey("pr_id"))
//                product.id = values.getAsLong("pr_id")
//
//
//        }
//    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeInt(amount)
        parcel.writeByte(if (isBought) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}