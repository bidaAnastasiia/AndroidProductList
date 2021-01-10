package com.example.productlist.product

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.productlist.AddElementActivity
import com.example.productlist.ProductListActivity
import com.example.productlist.databinding.ListElementBinding
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyAdapter(val context: Context, val list: ArrayList<Product>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("Product")
    var listOfKeys = ArrayList<String>()


    init {
        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildRemoved(snapshot: DataSnapshot) {
                CoroutineScope(IO).launch {
                    listOfKeys.remove(snapshot.key)
                    list.remove(getCopiedProduct(snapshot))
                    withContext(Main){
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                CoroutineScope(IO).launch {
                    list.add(getCopiedProduct(snapshot))
                    listOfKeys.add(snapshot.key!!)
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                CoroutineScope(IO).launch {
                    listOfKeys.remove(snapshot.key)
                    list.remove(getCopiedProduct(snapshot))
                    list.add(getCopiedProduct(snapshot))
                    listOfKeys.add(snapshot.key!!)
                    withContext(Main){
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        })
    }

    class MyViewHolder(val binding: ListElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListElementBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvName.text = list[position].name
        holder.binding.tvPrice.text = list[position].price
        holder.binding.tvAmount.text = list[position].amount.toString()
        holder.binding.tvIsBought.isChecked = list[position].isBought
        holder.binding.buttonEditProduct.setOnClickListener {
            val intent = Intent(context, AddElementActivity::class.java)
            intent.putExtra("current", list[position])
            intent.putExtra("currentKey",listOfKeys[position])
            ( context as ProductListActivity).finish()
            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int = list.size

    fun getCopiedProduct(snapshot: DataSnapshot): Product {
       var product = Product(
           name = snapshot.child("name").value as String,
           price = snapshot.child("price").value as String,
           amount = (snapshot.child("amount").value as Long).toInt(),
           isBought = snapshot.child("bought").value as Boolean
       )
        return product
    }
}